

			
		.ORIG x3000
		
		AND R0, R0, #0
		AND R1, R1, #0	;
		AND R2, R2, #0	;ABRA CADABRA MAKE THEM ALL ZERO!
		AND R3, R3, #0	;
		AND R4, R4, #0	
		AND R5, R5, #0	
		AND R6, R6, #0	
		AND R7, R7, #0	
	
		ADD R6, R6, #3	;R6 will be AGE counter
		ADD R4, R4, #7	;R4 will be UPI counter
	
;
;Display the message asking for UPI, and taking UPI input
;
		LEA R0, UPI
		PUTS
		LEA R1, UPIBLK	;Address of UPI storage block
	
LOOPUPI:	
		GETC		;Input to R0
		PUTC		;Print R0 to screen
		STR R0, R1, #0	;Memory address stored in R1 + 0
		ADD R1, R1, #1	;Increment the memory pointer, to point to next avaiable block
		ADD R4, R4, #-1	;Decrement R4, to ensure we only get 7 chars of input
		BRp LOOPUPI	;While R4 !=0 keep looping
	
;
;Display the message asking for AGE, and taking AGE input
;
		LEA R0, NEWLINE
		PUTS
		LEA R0, AGE
		PUTS
	
	
		
;
;Gets users AGE and store it
;
		LEA R1, AGEBLK
		
LOOPAGE		GETC
		PUTC
		ADD R5, R0, #-10 ;SUBTRACT 10 FROM INPUT TO CHECK FOR ENTER
		BRz CONVERT
		STR R0, R1, #0	;STORING THE CHAR IN MEMORY
		ADD R3, R3, #1	;KEEPING TRACK OF CHARS ENTERED, THIS IS USEFUL LATER
		ADD R1, R1, #1	;MEMORY POINTER FOR AGEBLK, INCREMENTS TO NEXT AVAIABLE BLOCK
		ADD R6, R6, #-1	;AGE LOOP CONDITION
		BRp LOOPAGE
		BRz WAITFORENTER

WAITFORENTER	GETC		
		ADD R5, R0, #-10 ;MAKE SURE ITS AN ENTER BEFORE MOVING ON
		BRz CONVERT	
		
;
;Calculates how many time to print UPI to screen -- CAP LOCKS ON.
;
		
CONVERT
		LEA R4, AGEBLK
		LD R1, INVERT
		ADD R4, R4, R3 ;AMOUNT OF NUMBERS
	
		ADD R3, R3, #-1
		BRz ONESONLY
	
		ADD R3, R3, #-1
		BRz TENS
	
		ADD R3, R3, #-1
		BRz ALL

ONESONLY	LDR R5, R4, #-1
		ADD R5, R5, R1
		ADD R2, R5, #0
		BRp PRINT

TENS		LDR R5, R4, #-1	;ONES
		ADD R5, R5, R1	;CONVERT FROM ASCII TO DEC
		BRnz CONTINUE	;IF ZERO WE DONT NEED TO ADD, AS WELL AS IF -VE
		ADD R2, R5, #0	;IF NOT ZERO, ADD TO R2
CONTINUE	
		LDR R5, R4, #-2	;TENS
		ADD R5, R5, R1	;CONVERT FROM ASCII TO DEC
LOOPFORTENS	
		ADD R2, R2, #10	;ADD 10 TO R2, FOR X AMOUNT OF TiMES
		ADD R5, R5, #-1
		BRp LOOPFORTENS
		BRz PRINT

ALL		LDR R5, R4, #-1	;ONES
		ADD R5, R5, R1	;CONVERT FROM ASCII TO DEC
		BRnz GONEXT	;IF ZERO/NEGATIVE DONT ADD GO TO TENS
		ADD R2, R5, #0	;IF ITS A NUMBER, ADD IT TO R2
GONEXT		
		LDR R5, R4, #-2	;TENS
		ADD R5, R5, R1	;CONVERT FROM ASCII TO DEC
		BRnz GOTOHUNDREDS;IF TENS ARE ZERO/NEGATIVE GO TO HUNDREDS
TENSLOOP	
		ADD R2, R2, #10	;ADD #10 TO R2, ITS ITS A NUMBER
		ADD R5, R5, #-1	;REDUCE R5, TO REDUCE LOOP
		BRp TENSLOOP
		BRz GOTOHUNDREDS
GOTOHUNDREDS	
		LDR R5, R4, #-3	;HUNDREDS
		ADD R5, R5, R1	;CONVERT FROM ASCII TO DEC
		BRz PRINT	;IF ZERO GO TO PRINT
		LD R1, HUNDRED	;LOAD R1 WITH #100
HUNDREDLOOP	
		ADD R2, R2, R1	;ADD #100 TO R2
		ADD R5, R5, #-1	;DECREMENT R5, VERY IMPORTANT
		BRp HUNDREDLOOP	
		BRz PRINT	;FINISH BASICALLY
;
;Prints UPI the required times to screen and then halts the processor
;

PRINT
		LEA R0, UPIBLK
		PUTS
		LEA R0, NEWLINE
		PUTS
		ADD R2, R2, #-1 ;FROM ABOVE, WE HAVE STORED AGE IN R2 
		BRp PRINT
		BRz DONE
	
;
;Make CPU go bye bye
;

DONE
		HALT




ASCII		.FILL x0030
UPI		.STRINGZ "Please enter your UPI: "
AGE		.STRINGZ "Please enter your age: "
NEWLINE		.STRINGZ "\n"
INVERT		.FILL #-48	
HUNDRED		.FILL #100
UPIBLK		.blkw 8
AGEBLK		.blkw 3
		.END