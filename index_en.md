![](https://ga-beacon.appspot.com/UA-102629055-1/nand2tetris/index_en?pixel)[![](https://img.shields.io/badge/license-GPL--3.0-blue.svg)](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/LICENSE) [![](https://img.shields.io/github/forks/ReionChan/nand2tetris.svg?style=social&label=Fork)](https://github.com/ReionChan/nand2tetris/network/members) [![](https://img.shields.io/github/stars/ReionChan/nand2tetris.svg?style=social&label=Star)](https://github.com/ReionChan/nand2tetris/stargazers)
  
## *The Elements of Computing Systems: Building a Modern Computer from First Principles*

 > Has been wondering how modern computers were built and saw [*Shimon Schocken*](http://shimonschocken.com/)'s presentation at [TED](https://www.youtube.com/watch?v=iE7YRHxwoDs), which introduced a course for students to build a modern computer that describes how modern computers is built from basic gate circuits. Each chapter introduces a project, one by one to complete these projects, you can construct a 16-bit Hack computer hardware emulator, based on this emulator, developed assembler and stacking virtual machine, designed Jack advanced programming language , As well as its compiler. Using the Java language to complete the various chapters of the project, and published to GitHub.
 
### [Contents](id:content-table)


* [Resource](#resource)
* [Projects](#projects)
	*  Typical hardware architecture (Chapter 1 ~ 5)
		* Boolean Logic			[`Project 01`](#project-01)
		* Boolean Arithmetic	[`Project 02`](#project-02)
		* Sequential Logic		[`Project 03`](#project-03)
		* Machine Language		[`Project 04`](#project-04)
		* Computer Architecture	[`Project 05`](#project-05)
	* Typical software architecture (Chapter 6 ~ 12)
		* Assembler		[`Project 06`](#project-06)
		* Virtual Machine [`Project 07`](#project-07) [`Project 08`](#project-08)
		* Compiler		[`Project 10`](#project-10) [`Project 11`](#project-11)
		* Operating System	[`Project 12`](#project-12)
		* High-Level Language	[`Project 09`](#project-09)
* [Copyright & License](#coli)

### [Resource](id:resource)

* This book: [The Elements of Computing Systems](http://www.amazon.com/Elements-Computing-Systems-Building-Principles/dp/0262640686/ref=ed_oe_p "购买") [^1]
* Official website:<http://www.nand2tetris.org>

* Course materials: <http://www.nand2tetris.org/course.php>

* Nand2Tetris Software: [Download](http://www.nand2tetris.org/software/nand2tetris.zip "Download")

	* [Setup Guide for macOS Users](http://www.nand2tetris.org/software/mac_guide.html "Setup Guide for Mac OS X by Yong Bakos")
	
	* Setup Guide for Windows Users
	
	> After downloading, put the downloaded zip file in an empty directory on your computer, and extract its contents as is, without changing the directories structure and names.
In order to use the nand2tetris software tools, your computer must be equipped with a **Java Run-time Environment**. The JRE can be downloaded freely from many sites including [this one](http://java.com/en/download/index.jsp). For best performance, download the latest available version. 


* Software Tools

Tool | Description | Tutorial
------------ | ------------- | ------------
Hardware Simulator | Simulates and tests logic gates and chips implemented in the HDL (Hardware Description Language) described in the book. Used in hardware construction projects.  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/Hardware%20Simulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/Hardware%20Simulator%20Tutorial.pdf)
CPU Emulator | Emulates the operation of the Hack computer system. Used to test and run programs written in the Hack machine language, in both its binary and assembly versions.  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/CPU%20Emulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/CPU%20Emulator%20Tutorial.pdf)
VM Emulator | Emulates the operation of our virtual machine (similar to Java's JVM); used to run and test programs written in the VM language (similar to Java's Bytcode).  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/VM%20Emulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/VM%20Emulator%20Tutorial.pdf)
Assembler | Translates programs from the Hack assembly language to Hack binary code. The resulting code can be executed directly on the Computer chip (in the hardware simulator), or emulated on the supplied CPU Emulator (much faster and more convenient).  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/Assembler%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/Assembler%20Tutorial.pdf)
Compiler | Translates programs written in Jack (a simple, Java-like object-based language) into VM code. The resulting code can run on the supplied VM Emulator. Alternatively, the VM code can be translated further by the supplied VM translator into Hack assembly code that can then be executed on the supplied CPU Emulator.  | (A GUI-less, command-level program)
Operating system | Translates programs written in Jack (a simple, Java-like object-based language) into VM code. The resulting code can run on the supplied VM Emulator. Alternatively, the VM code can be translated further by the supplied VM translator into Hack assembly code that can then be executed on the supplied CPU Emulator.  | (GUI-less)
Text Comparer | This utility checks if two input text files are identical, up to white space differences. Used in various projects. In Unix use "diff" instead.  | (A GUI-less, command-level program)


For more detail: <http://www.nand2tetris.org/software.php>

[`Return Contents`](#content-table)
### [Projects](id:projects) 
#### [[Project 01](https://github.com/ReionChan/nand2tetris/tree/master/projects/01)](id:project-01)
* Basic Logic Gates (1 bit)
	* [Not](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Not.hdl)
	
	* [And](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/And.hdl)
	
	* [Or](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or.hdl)
	
	* [Xor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Xor.hdl)
	
	* [Multiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux.hdl) [^2]
	
	* [DeMultiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux.hdl) [^3]
	
	
* Multi-Bit [^4] of Basic Gates (16 bit)
	* [Multi-Bit Not](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Not16.hdl)
	
	* [Multi-Bit And](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/And16.hdl)
	
	* [Multi-Bit Or](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or16.hdl)
	
	* [Multi-Bit Multiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux16.hdl)
	
	
* Multi-Way [^5] of Basic Gates
	* [8-Way Or](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or8Way.hdl)
	
	* [4-Way/16-Bit Multiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux4Way16.hdl)
	
	* [8-Way/16-Bit Multiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux8Way16.hdl)
	
	* [4-Way Demutiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux4Way.hdl)
	
	* [8-Way Demultiplexor](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux8Way.hdl)

[`Return Contents`](#content-table)
	
#### [[Project 02](https://github.com/ReionChan/nand2tetris/tree/master/projects/02)](id:project-02)

* Adders
	* [HalfAdder](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/HalfAdder.hdl)
	
	* [FullAdder](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/FullAdder.hdl)
	
	* [16-Bit Adder](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Add16.hdl)
	
	* [16-Bit Incrementer](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Inc16.hdl)
	
* The Arithmetic Logic Unit (ALU)
	* [16-Way Or](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Or16Way.hdl)
	
	* [16-Bit ALU](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/ALU.hdl)

[`Return Contents`](#content-table)
#### [[Project 03](https://github.com/ReionChan/nand2tetris/tree/master/projects/03)](id:project-03)

* [1-Bit Register](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/Bit.hdl)

* [16-Bit Register](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/Register.hdl)

* [RAM8 8-Registers](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/RAM8.hdl)

* [RAM64 64-Registers](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/RAM64.hdl)

* [RAM512 512-Registers](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM512.hdl)

* [RAM4K 4K-Registers](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM4K.hdl)

* [RAM16K 16K-Registers](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM16K.hdl)

* [Program Counter (PC)](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/PC.hdl)

[`Return Contents`](#content-table)
#### [[Project 04](https://github.com/ReionChan/nand2tetris/tree/master/projects/04)](id:project-04)

* [Multiplication Program](https://github.com/ReionChan/nand2tetris/blob/master/projects/04/mult/Mult.asm)

* [I/O-Handling Program](https://github.com/ReionChan/nand2tetris/blob/master/projects/04/fill/Fill.asm)

[`Return Contents`](#content-table)
#### [[Project 05](https://github.com/ReionChan/nand2tetris/tree/master/projects/05)](id:project-05)

* [Central Processing Unit (CPU)](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/CPU.hdl)

* [Memory](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/Memory.hdl)

* [Computer](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/Computer.hdl)

[`Return Contents`](#content-table)
#### [[Project 06](https://github.com/ReionChan/nand2tetris/tree/master/projects/06)](id:project-06)

* [Java Source file of Assembler](https://github.com/ReionChan/nand2tetris/tree/master/projects/06/Assembler)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Parser.java)
	
	* [Code.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Code.java) 
	
	* [SymbolTable.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/SymbolTable.java)
	
	* [Assembler.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Assembler.java)
	
* [汇编编译器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler.jar)

* Test Program

	* Adds the constants 2 and 3, puts the result in R0 [Add.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/add/Add.asm) 
	
	* Computes max(R0, R1), puts the result in R2
	
		* [MaxL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/max/MaxL.asm) (Symbol-less version)
		
		* [Max.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/max/Max.asm)
		
	* Draws a rectangle at the top left corner of the screen. 16 pixels wide and R0 pixels high.
		* [RectL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/rect/RectL.asm)(Symbol-less version)
		
		* [Rect.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/rect/Rect.asm)
		
	* A single-player Ping-Pong game.
	
		* [PongL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/pong/PongL.asm)(Symbol-less version)
		
		* [Pong.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/pong/Pong.asm)

[`Return Contents`](#content-table)
#### [[Project 07](https://github.com/ReionChan/nand2tetris/tree/master/projects/07)](id:project-07)

* [Java source files of VMTranslator, Step 1](https://github.com/ReionChan/nand2tetris/tree/master/projects/07/VMTranslator)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/Parser.java)
	
	* [CodeWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/CodeWriter.java)
		
	* [VMTranslator.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/VMTranslator.java)
	
* [Jar file of VMTranslator, Step 1](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator.jar)

* Test porgram

	* Stack Arithmetic
		
		* Pushes and adds two constants [SimpleAdd.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm)
		
		* Executes a sequence of arithimetic and logical operations on the stack. [StackTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/StackArithmetic/StackTest/StackTest.vm)
			
	* Memory Access
		
		* Executes pop and push operations using the virtual memory segments. [BasicTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/BasicTest/BasicTest.vm)
			
		* Executes pop and push operations using the pointer: `this`, `that` [PointerTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/PointerTest/PointerTest.vm)
		
		* Executes pop and push operations using the static segments. [StaticTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/StaticTest/StaticTest.vm)

[`Return Contents`](#content-table)
#### [[Project 08](https://github.com/ReionChan/nand2tetris/tree/master/projects/08)](id:project-08)

* [Java source files of VMTranslator, Step 2](https://github.com/ReionChan/nand2tetris/tree/master/projects/08/VMTranslator)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/Parser.java)
	
	* [CodeWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/CodeWriter.java)
		
	* [VMTranslator.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/VMTranslator.java)
	
* [Jar file of VMTranslator, Step 2](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator.jar)

* Test program

	* Test Programs for Program Flow Commands
		
		* Computes 1+2+...+n and pushes the result onto the stack. [BasicLoop.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/ProgramFlow/BasicLoop/BasicLoop.vm)
		
		* Fibonacci [FibonacciSeries.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm)
			
	* Test Programs for Function Calling Commands
		
		* Performs a simple calculation and returns the result. [SimpleFunction.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/SimpleFunction/SimpleFunction.vm)
			
		* Calling the nested Function [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/NestedCall/Sys.vm)
		
		* This program provides a full test of the implementation of the VM's function calling commands, the bootstrap section, and most of the other VM commands.
			
			* [Main.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/FibonacciElement/Main.vm)
			
			* [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/FibonacciElement/Sys.vm)
		
		* A full test of the VM implementation's handling of static variables.
			
			* [Class1.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Class1.vm)
			
			* [Class2.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Class2.vm) 
			
			* [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Sys.vm)

[`Return Contents`](#content-table)		
#### [[Project 09](https://github.com/ReionChan/nand2tetris/tree/master/projects/09)](id:project-09)

* [Greeting Program](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/Hello) `Author: Reion Chan`

	![Hello_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/Hello/Hello.gif "Hello_demo")

* [Countdown Timer](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/CountdownTimer) `Author: Reion Chan`

	![Countdown_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/CountdownTimer/Countdown.gif "Countdown_demo")


* [Rolling Ball](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/RollingBall) `Author: Reion Chan`
	
	![RollingBall_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/RollingBall/RollingBall.gif "RollingBall_demo")
	
	[![YouTube](https://github.com/ReionChan/nand2tetris/blob/master/res/youtube.png?raw=true "Watch This Demo")](https://www.youtube.com/watch?v=demDX8JKg0c) [Watch Demo in YouTube](https://www.youtube.com/watch?v=demDX8JKg0c)

* [Square](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/Square) `Author: Nand2Tetris.org`

	![Square_demo](https://github.com/ReionChan/nand2tetris/blob/master/projects/09/Square/Square.gif?raw=true "Square_demo")

* [Jack_OS_API.PDF](https://github.com/ReionChan/nand2tetris/blob/master/projects/09/Jack%20OS%20API.pdf)

[`Return Contents`](#content-table)
#### [[Project 10](https://github.com/ReionChan/nand2tetris/tree/master/projects/10)](id:project-10)

* [Java source files of JackAnalyzer](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/JackAnalyzer)

	* [CompilationEngine.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/CompilationEngine.java)
	
	* [JackTokenizer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/JackTokenizer.java) 
	
	* [XmlUtils.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/XmlUtils.java)
		
	* [JackAnalyzer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/JackAnalyzer.java)
	
* [Jar file of JackAnalyzer](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer.jar)

* Test program

	* [Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/Square)
	
	* [Expressionless Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/ExpressionlessSquare)
	
	* [Array Test](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/ArrayTest)

[`Return Contents`](#content-table)
#### [[Project 11](https://github.com/ReionChan/nand2tetris/tree/master/projects/11)](id:project-11)

* [Java source files of JackCompiler](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/JackCompiler)

	* [CompilationEngine.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/CompilationEngine.java)
	
	* [JackTokenizer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/JackTokenizer.java)
	
	* [SymbolTabel.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/SymbolTabel.java)
	
	* [VMWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/VMWriter.java)
		
	* [JackCompiler.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/JackCompiler.java)
	
* [Jar file of JackCompiler](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler.jar)

* Test program

	* [Seven](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Seven) Computes the value of (3*2)+1, and prints the result at the top left of the screen. Purpose: handles constans, arithmetic expression, do statement and return statement.
	
	* [Convert To Bin](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/ConvertToBin) Converts a 16-bit decimal number into its binary representation. Purpose: handles all procedural elements of the Jack language.
	
	* [Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Square) Purpose: Tests how your compiler handles the object-oriented constructs of the Jack language, such as constructors, methods, fields and expressions that include method calls. 
	
	* [Average](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Average) Purpose: handles arrays and strings
	
	* [Pong](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Pong) Purpose: handles objects and static variables
	
	* [Complex Arrays](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/ComplexArrays) Purpose: handles complex array references and expressions.

[`Return Contents`](#content-table)
#### [[Project 12](https://github.com/ReionChan/nand2tetris/tree/master/projects/12)](id:project-12)

* [Math.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Math.jack) 
  
	* [MathTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/MathTest)
	
* [String.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/String.jack) 
  
	* [StringTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/StringTest)
	
* [Array.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Array.jack)
  
	* [ArrayTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/ArrayTest)
	
* [Output.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Output.jack) 
  
	* [OutputTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/OutputTest)
	
* [Screen.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Screen.jack) 

	* [ScreenTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/ScreenTest)
	
* [Keyboard.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Keyboard.jack) 
  
	* [KeyboardTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/KeyboardTest)
	
* [Memory.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Memory.jack)
  
	* [MemoryTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/MemoryTest)
	
* [Sys.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Sys.jack) 
 
	* [SysTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/SysTest)


[`Return Contents`](#content-table)
### [Copyright & License](id:coli) 
```
Copyright 2015-2017 Reion Chan.

You are required to give attribution to the author (Reion Chan) for any
use of this program (GPLv3 Section 7b). 
Trying to pass off my code as your own in your Elements of Computing classes
will result in a cursed life of forever buggy software.
 
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

[`Return Contents`](#content-table)

[^1]: MIT Press, By [`Noam Nisan`](http://www.cs.huji.ac.il/~noam/) and [`Shimon Schocken`](http://shimonschocken.com/)

[^2]: [Multiplexor](https://en.wikipedia.org/wiki/Multiplexer) is a device that selects one of several analog or digital input signals and forwards the selected input into a single line.
	
[^3]: Demultiplexor take one data input and a number of selection inputs, and they have several outputs. 	
[^4]: `Multiple-Bit` Computer hardware is typically designed to operate on multi-bit arrays called “buses.” For example,a basic requirement of a 32-bit computer is to be able to compute (bit-wise) an And function on two given 32-bit buses.
	
[^5]: `Multiple-Way` accept multiple inputs
