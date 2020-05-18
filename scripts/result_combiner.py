import argparse
import os.path


def is_paths_correct(result_files: list) -> bool:
    for file_path in result_files:
        if not os.path.exists(file_path):
            print("Error: The path " + file_path + " is incorrect or does not exist")
            return False

    return True


def process(result_files: list, output_filename: str):
    if not is_paths_correct(result_files):
        return

    with open(output_filename, 'w') as output_file:
        for filename in result_files:
            with open(filename, 'r') as result_file:
                for line in result_file:
                    if len(line) > 0 and line != '\n':
                        output_file.write(line)


parser = argparse.ArgumentParser(description="Combine multiple result.csv files to one file.")
parser.add_argument("-r", nargs='+', dest="result_files",
                    help="[Required] Specify multiple paths to result.csv files using whitespace as delimiter")
parser.add_argument("-o", dest="output_filename", default="newResult.csv",
                    help="Specify the output filename")
args = parser.parse_args()
arg_result_files = args.result_files
arg_output_filename = args.output_filename
if arg_result_files is None:
    parser.print_help()
else:
    process(arg_result_files, arg_output_filename)
