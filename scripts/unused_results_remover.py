import argparse
import os.path
import re
from typing import Dict, Tuple


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


def _write_total(scans_filename: str, results_filename: str, total_results: Dict[str, Tuple[str, None or str]]):
    with open(scans_filename, "w") as scans_file, open(results_filename, "w") as results_file:
        for scan_line, result_line in total_results.values():
            if scan_line[1] != ' ':
                scans_file.write(scan_line)
                results_file.write(result_line)


def process(scans_filename: str, results_filename: None or str):
    total_results = _get_total_with_results(scans_filename, results_filename)
    _write_total("new" + scans_filename, "new" + results_filename, total_results)


parser = argparse.ArgumentParser(description="Remove unfilled scans.")
parser.add_argument("-s", action="store", dest="scans",
                    help="""[Required] Path to file with files to scan.
The file extension must be '.txt'.
Example file content _____: 
[ ] Benign_2016/com.vlcforandroid.vlcdirectprofree.apk
[ ] Benign_2016/com.williamsinteracti.jackpotparty.apk
# The line which starts with '#' symbol would not be processed
""")
parser.add_argument("-r", action="store", dest="results", help="Path to already processed file")
args = parser.parse_args()
arg_scans = args.scans
arg_results = args.results
if arg_scans is None or arg_results is None:
    parser.print_help()
else:
    process(arg_scans, arg_results)
