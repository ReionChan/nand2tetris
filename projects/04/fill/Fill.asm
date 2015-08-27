// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.
@i
M=0

(LOOP)
@KBD
D=M
@BLACK
D;JGT    // key>0 JUMP BLACK
@WHITE
D;JEQ    // key=0 JUMP WHITE

(BLACK)
@i
D=M
@8191
D=D-A
@LOOP
D;JGT   // if MAX Then do noting
@i
D=M
@SCREEN
A=A+D
M=-1
@i
M=M+1
@LOOP
0;JMP

(WHITE)
@i
D=M
@RESET
D;JLT   // if MIN Then do noting
@i
D=M
@SCREEN
A=A+D
M=0
@i
M=M-1
@LOOP
0;JMP

(RESET)
@i
M=0
@LOOP
0;JMP
