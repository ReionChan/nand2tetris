// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
// SET R2=0
@R2
M=0

// RO R1 > 0 
@R0
D=M
@END
D;JLE
@R1
D=M
@END
D;JLE

// SET R3=R1
@R1
D=M
@R3
M=D

// LOOP BODY 
(LOOP)
@R0
D=M
@R2
M=M+D
@R3
M=M-1
@R3
D=M
// R3=0 JUMP END
@END
D;JEQ
// R3>0 JUMP LOOP
@LOOP
D;JGT

// END PROGRAM
(END)
@END
0;JMP

