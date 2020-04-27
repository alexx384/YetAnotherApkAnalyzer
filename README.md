# YetAnotherApkAnalyzer

The purpose of this project is to develop analyzer that does the following work:
- extract properties from APK files to the CSV file;
- reduce the properties space;
- based on result properties, use machine-learning algorithms to classify Android apps as malware or benign.

# TODO:
[ ] Think about git-lfs for Gihub

# To prepare on Ubuntu 18.04
1. Download docker image of mobsf
```bash
docker pull opensecurity/mobile-security-framework-mobsf:latest
```
2. Install python3, pip, virtualenv module
```bash
sudo apt install python3 python3-pip python3-venv
```
3. Create python3 virtual environment for Androwarn and enter into
```bash
python3 -m venv venv
source venv/bin/activate
```
4. Install Androwarn requirements
```bash
pip install wheel
pip install -r androwarn/requirements.txt
```
5. Deactivate virtualenv
```dtd
deactivate
```
