
.ORIG X3000
BR START

;=======================================================================================; - ain't no one got time for LDI
AGEBAD		.STRINGZ "Invalid AGE, try again"
UPIBAD		.STRINGZ "Invalid UPI, try again"
UPI		.STRINGZ "Please enter your UPI: "
AGE		.STRINGZ "Please enter your age: "
R7STORE		.BLKW	2
a		.FILL #-97
z		.FILL #-122
NUMBER0		.FILL #-48
NUMBER9		.FILL #-57
UPIBLK		.BLKW	8
NEWLINE		.STRINGZ "\n"
;===================================START===============================================;
		
START		AND R0, R0, #0	
		AND R2, R2, #0
		AND R1, R1, #0	;
		AND R3, R3, #0	;PRESTO! ALL REGISTERS ARE NOW #0	
		AND R4, R4, #0	;
		AND R5, R5, #0
		AND R6, R6, #0
		AND R7, R7, #0			
		
;====================TAKE INTIAL INPUT OF UPI=============================;	
		ADD R6, R6, #-1
		ADD R3, R3, #1		;FOR PROMPTINPUT
		ADD R2, R2, #7		;MAX CHARS
		LEA R0, UPI
		LEA R1, UPIBLK
		PUTS

STARTUPI	GETC
		PUTC
		ADD R5, R0, #-10
		BRz CONTINUE
		STR R0, R1, #0
		ADD R1, R1, #1	 	;MEMORY POINTER, INCREMENT BY 1
		ADD R2, R2, #-1	 	;VALID INPUT IN TERMS OF NUMBER OF CHARS
		BRp STARTUPI
		BRz WAITFORENTERI	;CANT INPUT ANYMORE AT THIS POINT AS MAX CHARS HAVE BEEN REACHED
;===============================UPI========================================;

WAITFORENTERI	GETC		 ;AT THIS POINT, WE TAKE CHARS, BUT DONT STORE THEM
		PUTC 		
		ADD R5, R0, #-10 ;WE JUST WAIT FOR THE ENTER KEY BY THE USER
		BRz ENTERED	 ;WHEN WE GET THE ENTER, WE MOVE ON
		ADD R6, R6, #-1	 ;KEEPS TRACK OF KEYS PRESSED, INCLUDING ENTER, ENTER IS THEN REMOVED FROM THE COUNT
		BRnz WAITFORENTERI			

ENTERED		ADD R6, R6, #1
CONTINUE	ADD R2, R2, R6 	 ;AMOUNT OF KEYS PRESSED ADDED TO COUNT
		JSR PROMPTINPUT	 ; JSR -> PROMPTINPUT
		ADD R5, R5, #0	 ;CHECKS FOR 0 OR -1
		BRz TAKEAGE	 ;EVERYTHING CHECKS OUT WITH UPI, GO TO AGE
		BRn REDOUPI	 ;EVERYTHING DOES NOT CHECK OUT, GO GET UPI AGAIN
		
		
;=========================TAKE INITIAL INPUT OF AGE=========================;
TAKEAGE		AND R6, R6, #0		;THE LOOP COUNT, AMOUNT OF INPUT GIVEN TO US BY USER
		ADD R3, R3, #2		;FOR PROMPTINPUT
		ADD R4, R4, #3		;MAX CHARS
		LEA R0, AGE
		PUTS
		LEA R1, AGEBLK

STARTAGE	GETC
		PUTC
		ADD R5, R0, #-10	;CHECKING FOR ENTER
		BRz PROCESS		;ENTER HAS BEEN PRESSED, JUMP TO PROCESSING
		STR R0, R1, #0		;STORING CHARS
		ADD R6, R6, #1		;ADDING TO INPUT TAKEN
		ADD R1, R1, #1		;MEMORY POINTER INCREASE
		ADD R4, R4, #-1		;DECREMENT LOOP COUNTER
		BRp STARTAGE		
		BRz WAITFORENTERII	;IF LOOP HAS GONE 3 TIMES, WE JUMP HERE AND WAIT FOR ENTER

;====================AGE===================;	

WAITFORENTERII	GETC
		PUTC
		ADD R5, R0, #-10
		BRz PROCESS
		ADD R6, R6, #1		;KEEPS TRACK OF KEYS PRESSED BY USER THAT [ARE NOT] ENTER 
		BRp WAITFORENTERII


PROCESS		LEA R1, USERAGE
		STR R6, R1, #0
		JSR PROMPTINPUT
		ADD R5, R5, #0
		BRz CONVERT
		BRn REDOAGE 
		

;====================*PROMPTINPUT*===================; - Sweet jesus, let this work. 
PROMPTINPUT	LEA R0, R7STORE		;WE NEED TO STORE THE VALUE IN R7, IT CONTAINS THE ADDRESS OF OUR JSR + 1
		STR R7, R0, #0		;WE JUMP BACK THERE AFTER THE FUNCTION HAS FINISHED
		
		ADD R3, R3, #-1		;R3 IS BEING USED AS NAVAGATION FOR THE PROMPTINPUT FUNCTIION
		BRz UPIR		;R3 = 1 MEANS UPI CHECK, R3 = 2 MEANS AGE CHECK, R3 CAN ONLY GO TO AGE, IF UPI HAS
		BRp AGER		;HAS BEEN CLEARED AS CORRECT AND VALID, OTHERWISE IT WILL JUST KEEP ASKING THE USER TO 
					;REDO IT
UPIR		ADD R2, R2, #0		;R2 HOLDS THE AMOUNT OF CHARS ENTERED INTO THE PROMPT, NEGATIVE MEANS BUFFER OVERFLOW
		BRn TOOMANYUPI		;AND TELLS THE USER TO REDO THE INPUT
		BRz CHECKINPUTUPI	;ZERO MEANS THE AMOUNT OF CHARS MATCHES THE REQUIRED TOTAL

AGER		ADD R6, R6, #-1		;TAKE AWAY 1 FROM R6, WHICH HOLDS THE AMOUNT OF AGE CHARS ENTERED
		BRz CHECKINPUTAGE	;IF ITS ZERO, ONE CHAR HAS BEEN ENETRED, ASSUMING R6 IS NOW ZERO
		ADD R6, R6, #-1		;TAKE AWAY ONE AGAIN, TWO CHARS HAVE BEEN ENETERED ASSUMING R6 IS NOW ZERO
		BRz CHECKINPUTAGE	
		ADD R6, R6, #-1	
		BRz CHECKINPUTAGE	;TAKE AWAY ONE AGAIN, THREE CHARS HAVE BEEN ENETERED, ASSUMING R6 IS NOW ZERO
		BRp TOOMANYAGE		;IF NOT, THE USER HAS GIVEN MORE INPUT THAN REQUIRE -> FAIL ON AGE INPUT
		
;===============TOO MANY CHARS ERROR===============;

TOOMANYUPI	LEA R0, TOOMANYCHARS	;LOAD MESSAGE
		PUTS			
		LEA R0, NEWLINE		;THIS PART OF PROMPTINPUT TELLS THE USER THEY HAVE ENETERED TOO MANY CHARS FOR THE UPI		
		PUTS			;IT THEN SENDS THEM BACK TO THE UPI INPUT SECTION
		BR START

;===============RE ENTER UPI ERROR=================;

REDOUPI		LEA R0, UPIBAD		;THIS PART OF PROMPTINPUT TELLS THE USER THEY HAVE ENETERED AN INVALID UPI
		PUTS			;IT THEN DISPLAYS THE MESSAGE AND BRANCHES TO THE UPI INPUT SECTION
		LEA R0, NEWLINE
		PUTS
		BR START
	
;==============TOO MANY CHARS ERROR(AGE)============;

TOOMANYAGE	LEA R0, TOOMANYCHARS	;WITH THIS LOOP, WE TELL THE USER THEY HAVE ENTERED TOO MANY CHARS FOR THE AGE
		PUTS			;IT LOADS THE MESSAGE, PRINTS IT, AND THEN BRANCHES TO THE AGE INPUT SECTION
		LEA R0, NEWLINE
		PUTS

		AND R4, R4, #0		
		AND R3, R3, #0
		BR TAKEAGE		;JUMPS BACK TO TAKE INPUT			
		
			
;==============RE ENTER AGE ERROR===================;

REDOAGE		AND R4, R4, #0		;RESET THE AGE LOOP COUNTER
		AND R3, R3, #0		;RESET OUR PROMPTINPUT NAVAGATOR TO 0
		
		LEA R0, AGEBAD		;R0 NOW HOLDS ADDRESS OF AGEBAD
		PUTS
		LEA R0, NEWLINE		;R0 HOLDS ADDRESS OF NEWLINE
		PUTS
		BR TAKEAGE 		;BRANCH TO STARTAGE

;==============CHECK USERS UPI====================;

CHECKINPUTUPI	AND R2, R2, #0		;SET R2 TO 0, WE WILL USE IT TO KEEP TRACK OF THIS LOOP NOW, AS WE HAVE 7 CHARS INPUT
		ADD R2, R2, #7		;SET R2 TO 7, TO MATCH THE AMOUNT OF INPUT RECEIEVED
		ADD R2, R2, #-3		;WE ARE ONLY INTERESTED IN THE CHARACTERS, NOT DIGITS AT THE MOMENT
		LEA R0, UPIBLK		;LOAD THE ADDRESS OF UPIBLK INTO R0
		
NEXTONLISTCHAR	LDR R7, R0, #0		;LOAD THE VALUE CONTAINED IN THE ADDRESS HELD BY R0, INTO R7		
		ADD R0, R0, #1		;INCREMENT THE MEMORY POINTER TO THE NEXT CHAR BLOCK
		LD R5, a		;LOAD THE ASCII VALUE OF 'a' INTO R5
		ADD R7, R7, R5		;SUBTRACT ASCII FOR 'a' FROM INPUT IN R7, IF ITS NEGATIVE, NOT CORRECT INPUT		
		BRn INVALID
		LD R5, z		;LOADS THE ASCII VALUE OF 'z' INTO R5
		ADD R7, R7, R5		;SUBTRACT ASCII FOR 'z' FROM INPUT IN R7, IF +VE, NOT CORRECT
		BRp INVALID
		ADD R2, R2, #-1		;DECREMENT THE LOOP COUNTER
		BRp NEXTONLISTCHAR
	
	   ;==~*UPI DIGITS CHECK*~==; - such graphics	
		
		ADD R2, R2, #3		;THIS PART CHECKS THE UPI DIGITS
NEXTDIGIT	LDR R7, R0, #0		;LOADS THE CHAR, AT THE ADDRESS HELD BY R0, INTO R7
		ADD R0, R0, #1		;INCREMENT THE MEMORY POINTER TO THE NEXT CHAR
		LD R5, NUMBER0		;LOADS ASCII FOR '0', AND SUBTACTS FROM INPUT
		ADD R7, R7, R5		;ADD R5 TO R7, AND STORE THE VALUE INTO R7
		BRn INVALID
		LD R5, NUMBER9		;LOADS ASCII FOR '9', AND SUBTRACTS FROM INPUT
		ADD R7, R7, R5		;ADD R5 TO R7, STORE THAT VALUE INTO R7
		BRp INVALID
		ADD R2, R2, #-1		;DECREMENT THE LOOPER COUNTER
		BRp NEXTDIGIT		
		BRz GOOD		

;==============AGE INPUT CHECK================;																							<- man that is one hard wall of text to follow, sorry bro.		
		
CHECKINPUTAGE	LEA R0, AGEBLK		;LOAD THE ADDRESS OF AGEBLK INTO R0
		LEA R1, USERAGE		;LOAD THE ADDRESS OF USERAGE INTO R1
		LDR R6, R1, #0		;LOAD THE VALUE, HELD BY THE ADDRESS, WHICH IS STORED IN R1, INTO R6
		
AGECHECK	LDR R7, R0, #0		;LOAD THE VALUE HELD IN AGEBLK, INTO R7		
		ADD R0, R0, #1		;INCREMENT THE ADDRESS IN R0, TO POINT TO THE NEXT CHAR
		LD R5, NUMBER0		;LOAD (#-48) INTO R5
		ADD R3, R7, R5		;ADD R5 TO R7, AND STORE THAT VALUE IN R3
		BRn INVALID		;IF THE NUMBER IS NEGATIVE, INPUT IS INCORRECT
		LD R5, NUMBER9		;LOAD (#-57) INTO R5
		ADD R3, R7, R5		;ADD R5 TO R7, AND STORE THAT VALUE IN R3
		BRp INVALID		
		LD R5, a		;STORE THE VALUE OF 'a' INTO R5, 'a' IS IN ASCII
		ADD R7, R7, R5		;SUBTRACT ASCII FOR 'a' FROM INPUT IN R7, IF ITS POSITIVE, NOT CORRECT INPUT		
		BRp INVALID		
		LD R5, z		;LOAD #-122
		ADD R7, R7, R5		;SUBTRACT ASCII FOR 'z' FROM INPUT IN R7, IF ITS POSITIVE, NOT CORRECT INPUT AGAIN
		BRp INVALID
		ADD R6, R6, #-1		;DECREMENT R6, WHICH IS THE LOOP COUNTER
		BRp AGECHECK
		BRz GOOD

;====================RETURNS====================;

INVALID		AND R5, R5, #0		;SET R5 TO #0
		ADD R5, R5, #-1		;MAKES R5 #-1
		LEA R0, R7STORE		;LOAD THE ADDRESS OF R7STORE, INTO R0
		LDR R7, R0, #0		;LOAD INTO R7, THE VALUE INSIDE OF R0, WHICH IS JSR + 1
		RET			;RETURN TO THE ADDRESS OF JSR + 1

GOOD		AND R5, R5, #0		;RETURNS 0
		LEA R0, R7STORE		;LOAD ADDRESS OF R7STORE, INTO R0
		LDR R7, R0, #0		;LOAD INTO R7, THE VALUE INSIDE OF R0, WHICH IS JSR + 1
		RET			;RETURN TO THE ADDRESS OF JSR + 1

;==============PROMPTINPUT FINISHES==============;	
	
;================CONVERSION======================;

CONVERT		AND R3, R3, #0		;WILL HOLD THE AMOUNT OF INPUT RECEIEVED FROM USER, LOADED FROM MEMORY
		AND R4, R4, #0
		AND R5, R5, #0
		LEA R2, AGEBLK		;ADDRESS OF AGEBLK
		LEA R3, USERAGE		;AMOUNT OF INPUT USER GAVE US
		LDR R4, R3, #0		;HOLDS THE AMOUNT OF CHARS INPUT
		ADD R2, R2, R4		;SENDS US THE BACK OF AGEBLK
		LD R1, NUMBER0		;HOLDS #-48
	
		ADD R4, R4, #-1		
		BRz ONESONLY
	
		ADD R4, R4, #-1		;THESE 3 TEST TO SEE WHETHER WE ARE DEALING WITH 1, 2 OR 3 PLACES
		BRz TENS
	
		ADD R4, R4, #-1
		BRz ALL

ONESONLY	LDR R5, R2, #-1		;LOAD THE ONES -> CHAR AT THE END OF AGEBLK ASSUMING 1 CHAR INPUT
		ADD R5, R5, R1		;BECOMES DECIMAL
		ADD R6, R5, #0		;ADDS THE TEMP SUM IN R5, TO THE TOTAL SUM IN R6
		BRp PRINT

TENS		LDR R5, R2, #-1		;ONES -> END OF THE AGEBLK ASSUMING 2 CHAR INPUT
		ADD R5, R5, R1		;CONVERT FROM ASCII TO DEC
		BRnz CONTINUE1		;IF ZERO WE DONT NEED TO ADD, AS WELL AS IF -VE
		ADD R6, R5, #0		;IF NOT ZERO OR -VE, ADD TO R6
CONTINUE1	
		LDR R5, R2, #-2		;LOAD THE TENS -> CHAR IN THE FIRST SLOT ASSUMING 2 CHARS INPUT
		ADD R5, R5, R1		;CONVERT FROM ASCII TO DEC
LOOPFORTENS	
		ADD R6, R6, #10		;ADD 10 TO R6, FOR X AMOUNT OF TiMES
		ADD R5, R5, #-1
		BRp LOOPFORTENS
		BRz PRINT

ALL		LDR R5, R2, #-1		;ONES
		ADD R5, R5, R1		;CONVERT FROM ASCII TO DEC
		BRnz GONEXT		;IF ZERO/NEGATIVE DONT ADD GO TO TENS
		ADD R6, R5, #0		;IF ITS A NUMBER, ADD IT TO R6
GONEXT		
		LDR R5, R2, #-2		;TENS
		ADD R5, R5, R1		;CONVERT FROM ASCII TO DEC
		BRnz GOTOHUNDREDS	;IF TENS ARE ZERO/NEGATIVE GO TO HUNDREDS
TENSLOOP	
		ADD R6, R6, #10		;ADD #10 TO R6, IFS ITS A NUMBER
		ADD R5, R5, #-1		;REDUCE R5, TO REDUCE LOOP
		BRp TENSLOOP
		BRz GOTOHUNDREDS
GOTOHUNDREDS	
		LDR R5, R2, #-3		;HUNDREDS
		ADD R5, R5, R1		;CONVERT FROM ASCII TO DEC
		BRz PRINT		;IF ZERO GO TO PRINT
		LD R1, HUNDRED		;LOAD R1 WITH #100
HUNDREDLOOP	
		ADD R6, R6, R1		;ADD #100 TO R2
		ADD R5, R5, #-1		;DECREMENT R5, VERY IMPORTANT
		BRp HUNDREDLOOP	
		BRz PRINT		;FINISH BASICALLY

;======================PRINT======================;

PRINT		LEA R0, UPIBLK
		PUTS
		LEA R0, NEWLINE
		PUTS
		ADD R6, R6, #-1
		BRp PRINT
		BRz DONE
;=====================DONE========================;
DONE		HALT

;=================================================;

TOOMANYCHARS	.STRINGZ "You have entered too many characters, you must start again"
AGEBLK		.BLKW	3
LARGESTDIGIT	.FILL 	#57
USERAGE		.BLKW 1		
HUNDRED		.FILL #100
		.END