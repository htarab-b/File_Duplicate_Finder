# File Duplicate Finder

## Description

This Java program can find the duplicates of files and folders in the system.

## Getting Started

### Installing
* Create a java project in you system.
* Clone the repository inside the project directory.
```
git clone https://github.com/htarab-b/File_Duplicate_Finder.git
```

### Executing program
* Run 'App.java' to execute the program.
* Enter the path of the input file/folder.
* The program will start scanning all the files and directories in the system and duplicates of the input file/folder will be detected.

## Working Principle
* This Java program uses MD5 hashing algorithm to find the duplicates.
* If the given input is a file, The file is hashed and the hash value is stored in a variable.
* If the given input is a folder, all files inside the folder and subfolders will be hashed and and stored in an array.
* Once the input file/folder is hashed, the program will start scanning all files and folders in the system.
* The hash value of all scanned files/folders will be compared to the hash value of the input file/folder to find the duplicates.
* Because of hashing techniques involved in this program, even if the duplicate file has a different file name, the program will easily identify it by comparing the hash values of the content inside the file.
