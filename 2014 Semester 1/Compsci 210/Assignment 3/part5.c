//xx
//xx
//Assignment 3, part 5, compsci 210 
//goes to binary -> hex 
//Really messy code incoming..... 
//haters gonna hate, caters gonna cate, potatos gonna potate 

#include <stdio.h> 
#include <string.h> 
#include <stdlib.h> 
#include <ctype.h> 

//----------------Declarations------------------// 
FILE* fileIN; 
FILE* fileST; 
char final[17]; 
char commands[5][13]; 
char symbolslabel[50][10];
char memrylocs[50][10];
long programCounter; 
char startValue[5]; 

//-----------------Main-------------------------// 

int main (int argc, char* argv[]) { 
     
    //adding the operands into the char array 
    strcpy(commands[0], "and"); 
    strcpy(commands[1], "add"); 
    strcpy(commands[2], "jmp"); 
    strcpy(commands[3], "ld" ); 
    strcpy(commands[4], "br" ); 
     
    //lets set our program counter, DEC, of course 
    programCounter = strtol(argv[3], NULL, 16); 
    
    //lets open out files 
    fileIN = fopen(argv[1], "r"); 
    
	//open the symbol table and store it in an array
    if(argc == 4){ 
        fileST = fopen(argv[2], "r");
	}
     
    //lets check to see if the files have opened/been created as required 
    if(fileIN == NULL) { 
        printf("Error couldn't find file"); 
        return 1; 
    } 
     
    if(fileST == NULL){ 
        printf("Error couldn't open/find the symbol table file");
		return 1;
    } 
    char words[15], labels[5], values[5];
	int symbolsToLoop;
	short j;
	for(j = 0; j < 50; j++){
		if(!feof(fileST)){
			fgets(words, 15, fileST);
			sscanf(words, "%s %s", labels, values);
			strcpy(symbolslabel[j], labels);
			strcpy(memrylocs[j], values);
			symbolsToLoop++;
		}
	}
	 
/*-------------lets create a few variables---------------*/
    char line[14], addrs[9], label[5];
    char op[2], desR[2], secondR[3], lastR[2], brOperand[5], brLabel[7]; 
    char ldLabel[5];
	int value; 
    long offsetForLDs, result; 
	   
//---------------------Convert--------------------------//     
    while(fgets(line, 14, fileIN) && line[14] != EOF){ 
         
        if(line[0] == 'l' && line[1] == 'd'){ //check for both 'l' and 'd', incase we want to implement other ld functions later 
			programCounter++; 
            strcpy(final, "0010"); 
            line[2] = ','; 
            sscanf(line, "%[^','], %[^','], %[^',']", op, desR, ldLabel); 
            desR[0] = '0'; 
			
			/* Checking our array of symbols for the corresponding one */
			short m;
			for(m = 0; m < symbolsToLoop; m++){
				if((strcmp(ldLabel, symbolslabel[m]) - 13) == 0){
					strcpy(label, symbolslabel[m]);
					strcpy(addrs, memrylocs[m]);
				}
			}

			offsetForLDs = strtol(addrs, NULL, 16); 
            result = offsetForLDs - programCounter; 
		/* Grab the last 9 bits of the variable result */
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
        /* End of LD */
		
        if(line[0] == 'b' && line[1] == 'r'){
			programCounter++;
            strcpy(final, "0000"); 
			sscanf(line, "%s %s", brOperand, brLabel); 
			
		/* Condition Codes, lots of else if statements */
			if((line[2] == 'n') && (line[3] == 'z') && (line[4] == 'p')){strcat(final, "111");}
			else if((line[2] == 'n') && (line[3] == 'z')){strcat(final, "110");}
			else if((line[2] == 'n') && (line[3] == 'p')){strcat(final, "101");}
			else if((line[2] == 'z') && (line[3] == 'p')){strcat(final, "011");}
			else if(line[2] == 'n'){strcat(final, "100");}
			else if(line[2] == 'z'){strcat(final, "010");} /* we use else if, instead of if, because more than one */
			else if(line[2] == 'p'){strcat(final, "001");} /* can be true, and that would mess our output up */
			else if(line[2] == ' '){strcat(final, "000");line[2] = ',';}
	
		/* Checking our array of symbols for the corresponding one */
			short k;
			for(k = 0; k < symbolsToLoop; k++){
				if(strcmp(brLabel, symbolslabel[k]) == 0){
					strcpy(label, symbolslabel[k]);
					strcpy(addrs, memrylocs[k]);
				}
			}
			offsetForLDs = strtol(addrs, NULL, 16); 
            result = offsetForLDs - programCounter;

		/* Grab the last 9 bits of the variable result */
            short i; 
            addrs[9] = 0; 
            for( i = 8; i >= 0; i--){ 
				if( (1 << i ) & result){ 
					addrs[8-i] = '1'; 
                }else{ 
					addrs[8-i] = '0'; 
                } 
            }
			strcat(final, addrs);
			printf("0"); /* This correspinds to '0000' op code in hex */
			goto SKIP;
		}
		/* End of BR */
		
		if(line[0] == 'j'){ 
			programCounter++; 
            strcpy(final, "1100"); 
            line[3] = ','; //remove the space char, and replace it with a comma 
            sscanf(line, "%[^','], %[^',']", op, secondR); 
            desR[0] = '0'; 
            secondR[0] = '0'; 
        }
		/* End of JMP */ 
     
        if(line[0] == 'a'){ 
			programCounter++; 
            line[3] = ','; 
            sscanf(line, "%[^','], %[^','], %[^','], %[^',']", op, desR, secondR, lastR); 
            desR[0] = '0'; 
            secondR[0] ='0';
			
			if(strcmp(op, commands[0]) == 0) { //AND 
				strcpy(final, "0101"); 
			} 
			if(strcmp(op, commands[1]) == 0) { //ADD 
				strcpy(final, "0001"); 
			} 
        } 
        /* End of ADD/AND */
             
		/* Destination Register Values */
        switch(atoi(desR)){ 
            case 1 : strcat(final, "001");break; /* */ case 2 : strcat(final, "010");break; 
            case 3 : strcat(final, "011");break; /* */ case 4 : strcat(final, "100");break;  
            case 5 : strcat(final, "101");break; /* */ case 6 : strcat(final, "110");break; 
            case 7 : strcat(final, "111");break; /* */ case 0 : strcat(final, "000");break; 
        } 
        if(strcmp(op, commands[3]) == 0){ 
			strcat(final, addrs);             
            goto SKIP; 
        } 
        /* Second Register Values, Same as above */ 
        switch(atoi(secondR)){ 
            case 1 : strcat(final, "001");break; /* */ case 2 : strcat(final, "010");break; 
            case 3 : strcat(final, "011");break; /* */ case 4 : strcat(final, "100");break; 
            case 5 : strcat(final, "101");break; /* */ case 6 : strcat(final, "110");break;   
            case 7 : strcat(final, "111");break; /* */ case 0 : strcat(final, "000");break; 
        } 
         
        if(line[0] == 'j'){ //we need to jump the next few if's if the command is a jmp 
            goto LASTR; 
        } 
		/* Basically we check immediates here */
		if(lastR[0] != 'r'){  
            value = atoi(lastR); //commands are always correct, no need to check other things(15 to -16) 
            goto IMMD; 
         
        }else if(lastR[0] == 'r'){ 
            lastR[0] ='0'; 
            goto LASTR; //jumps to LASTR because the command is not immediate 
        } 
         /* Last Register Values, assumung non-immediate mode */
LASTR:    switch(atoi(lastR)){ 
            case 1 : strcat(final, "000001");goto SKIP; /* */ case 2 : strcat(final, "000010");goto SKIP; 
            case 3 : strcat(final, "000011");goto SKIP; /* */ case 4 : strcat(final, "000100");goto SKIP; 
            case 5 : strcat(final, "000101");goto SKIP; /* */ case 6 : strcat(final, "000110");goto SKIP; 
            case 7 : strcat(final, "000111");goto SKIP; /* */ case 0 : strcat(final, "000000");goto SKIP; 
							/* Here we don't need to worry about bit 5, its always zero */ 
			 
        } 
         /* This holds all our possible 6 bit 2's comp values (15 to -16) */
IMMD: 		switch(value){ 
				 /* bits are numbered from 0 -> 5 and bit 5 is always 1 - immediate mode */
            case  0 : strcat(final, "100000");break; /* */ case -1 : strcat(final, "111111");break; 
            case -2 : strcat(final, "111110");break; /* */ case -3 : strcat(final, "111101");break;  
            case -4 : strcat(final, "111100");break; /* */ case -5 : strcat(final, "111011");break; 
            case -6 : strcat(final, "111010");break; /* */ case -7 : strcat(final, "111001");break; 
            case -8 : strcat(final, "111000");break; /* */ case -9 : strcat(final, "110111");break; 
            case -10: strcat(final, "110110");break; /* */ case -11: strcat(final, "110101");break; 
            case -12: strcat(final, "110100");break; /* */ case -13: strcat(final, "110011");break; 
            case -14: strcat(final, "110010");break; /* */ case -15: strcat(final, "110001");break; 
            case -16: strcat(final, "110000");break; /* */ case 1  : strcat(final, "100001");break; 
            case 2  : strcat(final, "100010");break; /* */ case 3  : strcat(final, "100011");break; 
            case 4  : strcat(final, "100100");break; /* */ case 5  : strcat(final, "100101");break; 
            case 6  : strcat(final, "100110");break; /* */ case 7  : strcat(final, "100111");break; 
            case 8  : strcat(final, "101000");break; /* */ case 9  : strcat(final, "101001");break; 
            case 10 : strcat(final, "101010");break; /* */ case 11 : strcat(final, "101011");break; 
            case 12 : strcat(final, "101100");break; /* */ case 13 : strcat(final, "101101");break; 
            case 14 : strcat(final, "101110");break; /* */ case 15 : strcat(final, "101111");break; 
        } 
SKIP:   /* Checking to make sure it is a proper 16 bit command */    
        if(strlen(final) == 16){ 
			printf("%x\n", strtol(final, NULL, 2)); 
        } 
		//reset all the variables used in the loop, ready for the next loop 
        memset(final, 0, sizeof(final)); /* */ memset(op, 0, sizeof(op));
		memset(lastR, 0, sizeof(lastR)); /* */ memset(desR, 0, sizeof(desR));
        memset(addrs, 0, sizeof(addrs)); /* */ memset(brOperand, 0, sizeof(brOperand));
        memset(label, 0, sizeof(label)); /* */ memset(brLabel, 0, sizeof(brLabel)); 
    } 
    /* Be a tidy kiwi - close your shit */   
    fclose(fileIN); 
    return 0; 
}

