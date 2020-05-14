import argparse
import os.path
import re
from typing import Dict, Tuple


def _get_total_without_results(total_scans_filename: str) -> Dict[str, Tuple[str, None]]:
    pattern = re.compile(r".+/(.+)")
    total_dict = dict()
    comments = str()
    with open(total_scans_filename, "r") as file:
        for line in file:
            if line[0] == '#':
                comments += line
                continue
            result = pattern.match(line)
            scan_filename = result.group(1)
            total_dict[scan_filename] = (line, None)

    total_dict["/"] = (comments, None)
    return total_dict


def _get_total_with_results(total_scans_filename: str, total_results_filename: str) \
        -> Dict[str, Tuple[str, None or str]]:
    pattern = re.compile(r".+/(.+)")
    total_dict = dict()
    comments = str()
    with open(total_scans_filename, "r") as scans_file, open(total_results_filename, "r") as results_file:
        for scans_line in scans_file:
            if scans_line[0] == '#':
                comments += scans_line
                continue
            result = pattern.match(scans_line)
            scan_filename = result.group(1)
            results_line = results_file.readline()
            total_dict[scan_filename] = (scans_line, results_line)

    total_dict["/"] = (comments, None)
    return total_dict


def _get_total(total_scans_filename: str, total_results_filename: None or str) -> Dict[str, Tuple[str, None or str]]:
    if total_results_filename is None or os.path.exists(total_results_filename) is None:
        return _get_total_without_results(total_scans_filename)
    else:
        return _get_total_with_results(total_scans_filename, total_results_filename)


def _write_total(scans_filename: str, results_filename: str, total_results: Dict[str, Tuple[str, None or str]]):
    with open(scans_filename, "w") as scans_file, open(results_filename, "w") as results_file:
        for scan_line, result_line in total_results.values():
            scans_file.write(scan_line)
            if result_line is None:
                results_file.write('\n')
            else:
                results_file.write(result_line)


def process(parser_stdout_filename: str, parser_result_csv_filename: str, total_scans_filename: str,
            total_results_filename: None or str):
    total_results = _get_total(total_scans_filename, total_results_filename)
    pattern = re.compile(r"\[\+\].+/(.+)|.+|$")
    with open(parser_stdout_filename, "r") as out_file, open(parser_result_csv_filename, "r") as result_file:
        for out_line in out_file:
            result = pattern.match(out_line)
            processed_file = result.group(1)
            if processed_file is None:
                continue
            result = total_results[processed_file]
            result_line = result_file.readline()
            if result is None:
                continue
            scan_line, _ = result
            scan_line = scan_line[:1] + 'X' + scan_line[2:]
            total_results[processed_file] = (scan_line, result_line)

    if total_results_filename is None:
        total_results_filename = total_scans_filename[:-4] + ".csv"

    _write_total("new" + total_scans_filename, "new" + total_results_filename, total_results)


parser = argparse.ArgumentParser(description="Convenient process parser results.")
parser.add_argument("-ps", action="store", dest="parser_stdout_filename",
                    help="[Required] Path to file with parser's output to stdout")
parser.add_argument("-pr", action="store", dest="parser_result_csv_filename",
                    help="[Required] Path to file with parser's result.csv")
parser.add_argument("-ts", action="store", dest="total_scans_filename",
                    help="""[Required] Path to file with files to scan.
The file extension must be '.txt'.
Example file content _____: 
[ ] Benign_2016/com.vlcforandroid.vlcdirectprofree.apk
[ ] Benign_2016/com.williamsinteracti.jackpotparty.apk
# The line which starts with '#' symbol would not be processed
""")
parser.add_argument("-tr", action="store", dest="total_results_filename", help="Path to already processed file")
args = parser.parse_args()
arg_parser_stdout_filename = args.parser_stdout_filename
arg_parser_result_csv_filename = args.parser_result_csv_filename
arg_total_scans_filename = args.total_scans_filename
arg_total_results_filename = args.total_results_filename
if arg_parser_stdout_filename is None or arg_parser_result_csv_filename is None or arg_total_scans_filename is None:
    parser.print_help()
else:
    process(arg_parser_stdout_filename, arg_parser_result_csv_filename, arg_total_scans_filename,
            arg_total_results_filename)
