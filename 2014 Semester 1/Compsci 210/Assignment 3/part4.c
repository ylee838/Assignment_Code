//xx
//xx
//Assignment 3, part 4, compsci 210
//goes to binary -> hex
//Really messy code incoming.....

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//----------------Declarations------------------//
FILE* fileIN;
FILE* fileST;
char final[17];
char commands[4][13];
long programCounter;
char startValue[5];

//-----------------Main-------------------------//

int main (int argc, char* argv[]) {
	
	//adding the operands into the char array
	strcpy(commands[0], "and");
	strcpy(commands[1], "add");
	strcpy(commands[2], "jmp");
	strcpy(commands[3], "ld" );
	
	//lets set our program counter, DEC, of course
	programCounter = strtol(argv[3], NULL, 16);
	
	
	//lets open out files
	fileIN = fopen(argv[1], "r");
	
	if(argc == 4){
		fileST = fopen(argv[2], "r");
	}
	
	//lets check to see if the files have opened/been created as required
	if(fileIN == NULL) {
		printf("Error couldn't find file");
		return 1;
	}
	if(!fileST){
		printf("Error couldn't find file");
		return 1;
	}

	//lets create a char array that will hold the line
	char line[14], otherLine[8], addrs[9], label[3];
	char op[2], desR[2], secondR[3], lastR[2];
	int value;
	long offsetForLDs, result;
	
//---------------------Convert--------------------------//	
	while(fgets(line, 14, fileIN) && line[14] != EOF){
			
		if(line[0] == 'j'){ //scans the jmp command and splits operands
			programCounter++;
			strcpy(final, "1100");
			line[3] = ','; //remove the space char, and replace it with a comma
			sscanf(line, "%[^','], %[^',']", op, secondR);
			desR[0] = '0';
			secondR[0] = '0';
			goto CC; //Command Check
		}
		
		if(line[0] == 'l' && line[1] == 'd'){ //check for both 'l' and 'd', incase we want to implement other ld functions later
			programCounter++;
			strcpy(final, "0010");
			line[2] = ',';
			sscanf(line, "%[^','], %[^','], %[^',']", op, desR, secondR);
			desR[0] = '0';
						
			
			if(fgets(otherLine, 9, fileST) && otherLine[9] != EOF){
				sscanf(otherLine, "%s %s", label, addrs);
				offsetForLDs = strtol(addrs, NULL, 16);
				result = offsetForLDs - programCounter;
				
				short i;
				addrs[9] = 0;
				for( i = 8; i >= 0; i--){
					if( (1 << i ) & result){
						addrs[8-i] = '1';
					}else{
						addrs[8-i] = '0';
					}
				}
			}	
		}
	
		
		if(line[0] == 'a'){ //scans and/add commands, splits operands
			programCounter++;
			line[3] = ','; //remove the space char, and replace it with a comma
			sscanf(line, "%[^','], %[^','], %[^','], %[^',']", op, desR, secondR, lastR);
			desR[0] = '0'; //we know the first letter in each of these will be 'r'
			secondR[0] ='0';//so lets get rid of 'r'
		}
	
		
CC:					
		if(strcmp(op, commands[0]) == 0) { //AND
			strcpy(final, "0101");
		}

		if(strcmp(op, commands[1]) == 0) { //ADD
			strcpy(final, "0001");
		}


		switch(atoi(desR)){
			case 1 : strcat(final, "001");break; case 2 : strcat(final, "010");break;
			case 3 : strcat(final, "011");break; case 4 : strcat(final, "100");break; 
			case 5 : strcat(final, "101");break; case 6 : strcat(final, "110");break;
			case 7 : strcat(final, "111");break; case 0 : strcat(final, "000");break;
		}
		if(strcmp(op, commands[3]) == 0){
			strcat(final, addrs);
			goto SKIP;
		}
		
		switch(atoi(secondR)){
			case 1 : strcat(final, "001");break; case 2 : strcat(final, "010");break;
			case 3 : strcat(final, "011");break; case 4 : strcat(final, "100");break;
			case 5 : strcat(final, "101");break; case 6 : strcat(final, "110");break;  
			case 7 : strcat(final, "111");break; case 0 : strcat(final, "000");break;
		}
		
		if(line[0] == 'j'){ //we need to jump the next few if's if the command is a jmp
			goto LASTR;
		}
		
		if(lastR[0] != 'r'){ 
			value = atoi(lastR); //commands are always correct, no need to check other things(15 to -16)
			goto FINISH; //jumps to FINISH only if the immediate value is correct
		
		}else if(lastR[0] == 'r'){
			lastR[0] ='0';
			goto LASTR; //jumps to LASTR because the command is not immediate
		}
		
LASTR:	switch(atoi(lastR)){ //bits numbered from 0 -> 5
			case 1 : strcat(final, "000001");goto SKIP; case 2 : strcat(final, "000010");goto SKIP;
			case 3 : strcat(final, "000011");goto SKIP; case 4 : strcat(final, "000100");goto SKIP;
			case 5 : strcat(final, "000101");goto SKIP; case 6 : strcat(final, "000110");goto SKIP;
			case 7 : strcat(final, "000111");goto SKIP; case 0 : strcat(final, "000000");goto SKIP;
			//here we need not worry about bit 5, its always zero
		}
		
FINISH: switch(value){ //this holds all our possible 6 bit 2's comp values (15 to -16)
			//bits are numbered from 0 -> 5
			case  0 : strcat(final, "100000");break; case -1 : strcat(final, "111111");break;
			case -2 : strcat(final, "111110");break; case -3 : strcat(final, "111101");break; 
			case -4 : strcat(final, "111100");break; case -5 : strcat(final, "111011");break;
			case -6 : strcat(final, "111010");break; case -7 : strcat(final, "111001");break;
			case -8 : strcat(final, "111000");break; case -9 : strcat(final, "110111");break;
			case -10: strcat(final, "110110");break; case -11: strcat(final, "110101");break;
			case -12: strcat(final, "110100");break; case -13: strcat(final, "110011");break;
			case -14: strcat(final, "110010");break; case -15: strcat(final, "110001");break;
			case -16: strcat(final, "110000");break; case 1  : strcat(final, "100001");break;
			case 2  : strcat(final, "100010");break; case 3  : strcat(final, "100011");break;
			case 4  : strcat(final, "100100");break; case 5  : strcat(final, "100101");break;
			case 6  : strcat(final, "100110");break; case 7  : strcat(final, "100111");break;
			case 8  : strcat(final, "101000");break; case 9  : strcat(final, "101001");break;
			case 10 : strcat(final, "101010");break; case 11 : strcat(final, "101011");break;
			case 12 : strcat(final, "101100");break; case 13 : strcat(final, "101101");break;
			case 14 : strcat(final, "101110");break; case 15 : strcat(final, "101111");break;
			//here bit 5 HAS to be 1, as we are using immediate mode, therefore the first bit from the left
			//is 1, no matter what
		}

		
		
SKIP:		
		if(strlen(final) == 16){ //prevent 0's from printing in file
			printf("%x\n", strtol(final, NULL, 2));
		}
		memset(final, 0, sizeof(final));
		memset(op, 0, sizeof(op));
		memset(desR, 0, sizeof(desR));
		memset(secondR, 0, sizeof(secondR));
		memset(lastR, 0, sizeof(lastR));
		memset(addrs, 0, sizeof(addrs));
	}
		//reset all the variables used in the loop, ready for the next loop

	fclose(fileIN);
	return 0;
	}