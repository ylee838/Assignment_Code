//xx
//xx
//Assignment 3, part 1, compsci 210

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//----------------Declarations------------------//

FILE* fileIN;
FILE* fileOUT;
char final[17];
char commands[2][13];

//-----------------Main-------------------------//

int main (int argc, char* argv[]) {
	//adding the operands into the char array
	strcpy(commands[0], "and");
	strcpy(commands[1], "add");
	
	//lets open out files
	fileIN = fopen(argv[1], "r");
	fileOUT = fopen("output.txt", "wa+");

	//lets check to see if the files have opened/been created as required
	if(fileIN == NULL) {
		printf("Error couldn't find file");
		return 1;
	}

	if(fileOUT == NULL) {
		printf("Error couldn't open/create the output file -> output.txt in root directory");
		return 1;
	}
	//lets create a char array that will hold the line
	char line[13];
	char op[2], desR[2], secondR[2], lastR[2];

	while(fgets(line, 13, fileIN) && line[13] != EOF) {
		
		line[3] = ','; //remove the space char, and replace it with a comma
		
		//scanning the char array 'line' and splitting it into varaiables when it hits a ','
		
		sscanf(line, "%[^','], %[^','], %[^','], %[^',']", op, desR, secondR, lastR);
		
		desR[0] = '0'; //we know the first letter in each of these will be 'r'
		secondR[0] ='0';//so lets get rid of 'r'
		lastR[0] ='0';
		
		if(strcmp(op, commands[0]) == 0) { //AND
			strcpy(final, "0101");
		}

		if(strcmp(op, commands[1]) == 0) { //ADD
			strcpy(final, "0001");
		}

		switch(atoi(desR)){
			case 1 : strcat(final, "001"); break;
			case 2 : strcat(final, "010"); break;
			case 3 : strcat(final, "011"); break;
			case 4 : strcat(final, "100"); break;
			case 5 : strcat(final, "101"); break;
			case 6 : strcat(final, "110"); break;
			case 7 : strcat(final, "111"); break;
			case 0 : strcat(final, "000"); break;
		}

		switch(atoi(secondR)){
			case 1 : strcat(final, "001"); break;
			case 2 : strcat(final, "010"); break;
			case 3 : strcat(final, "011"); break;
			case 4 : strcat(final, "100"); break;
			case 5 : strcat(final, "101"); break;
			case 6 : strcat(final, "110"); break;
			case 7 : strcat(final, "111"); break;
			case 0 : strcat(final, "000"); break;
		}

		switch(atoi(lastR)){ //16 bits, therefore the last bits must make up 6.
			case 1 : strcat(final, "000001"); break;
			case 2 : strcat(final, "000010"); break;
			case 3 : strcat(final, "000011"); break;
			case 4 : strcat(final, "000100"); break;
			case 5 : strcat(final, "000101"); break;
			case 6 : strcat(final, "000110"); break;
			case 7 : strcat(final, "000111"); break;
			case 0 : strcat(final, "000000"); break;
		}
		
		if(strlen(final) == 16){ //prevent 0's from printing in file
			
			fprintf(fileOUT, "%x\n" ,strtol(final, NULL, 2));
		}
		
		memset(final, 0, sizeof(final));
		memset(op, 0, sizeof(op));
		memset(desR, 0, sizeof(desR));
		memset(secondR, 0, sizeof(secondR));
		memset(lastR, 0, sizeof(lastR));
	}
	fclose(fileIN);
	fclose(fileOUT);
	return 0;
}
