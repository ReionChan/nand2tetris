## 《计算机系统要素-从零开始构建现代计算机》[^1]

 > 之前一直有想了解现代计算机是如何构建起来的，偶然间看到[`Shimon Schocken`](http://shimonschocken.com/)在[TED](https://www.youtube.com/watch?v=iE7YRHxwoDs)的一期演讲，介绍其为学生开发了一套逐步构建现代计算机的课程，该课程让大家了解现代计算机如何从基本门电路开始慢慢被构建。全书以项目为单元，逐一完成这些项目，即可构造出一个16位的Hack计算机硬件，在此硬件之上，开发出汇编编译器、堆栈式虚拟机，针对虚拟机设计出高级编程语言Jack，同时开发出相应的Jack语言编译器。真是相见恨晚，细读的同时根据规范采用Java语言完成了各个章节项目，故而有了此GitHub上的项目代码。
 
### [Content Table](id:content-table)
（目录）

* [Resource （资源）](#resource)
* [Projects（项目）](#projects)
	* 典型的硬件体系 （第一章 ~ 第五章）
		* 布尔逻辑		[`Project 01`](#project-01)
		* 布尔算术 		[`Project 02`](#project-02)
		* 时序逻辑		[`Project 03`](#project-03)
		* 机器语言		[`Project 04`](#project-04)
		* 计算机体系结构	[`Project 05`](#project-05)
	* 典型的软件体系 （第六章 ~ 第十二章）
		* 汇编编译器		[`Project 06`](#project-06)
		* 虚拟机			[`Project 07`](#project-07) [`Project 08`](#project-08)
		* 编译器			[`Project 10`](#project-10) [`Project 11`](#project-11)
		* 操作系统		[`Project 12`](#project-12)
		* 高级语言/应用程序	[`Project 09`](#project-09)
* [Errata （中文版勘误）](#errata)
* [Copyright （版权）](#copyright)
* [Licence （许可证）](#licence)

### [Resource](id:resource)
（资源）

* 原书：[The Elements of Computing Systems](http://www.amazon.com/Elements-Computing-Systems-Building-Principles/dp/0262640686/ref=ed_oe_p "购买") [^2]
* 官网：<http://www.nand2tetris.org>

* 官网课件：<http://www.nand2tetris.org/course.php>

* Nand2tetris软件: [下载](http://www.nand2tetris.org/software/nand2tetris.zip "Download")

	* [Mac OS X 配置](http://www.nand2tetris.org/software/mac_guide.html "Setup Guide for Mac OS X by Yong Bakos")
	
	* Windows 配置
	
	> 下载后, 将zip文件放到电脑上的一个空目录里然后解压，保持解压缩的文件、目录的名称和结构不变。运行该软件的电脑必需包含`JRE（Java运行环境）`，JRE可以在很多网站免费下载，例如[Java官网](http://java.com/zh_CN)。为了更好的性能，请确保下载最新本。

* 软件工具

工具 | 描述 | 教程下载
------------ | ------------- | ------------
硬件仿真器 | 采用HDL描述的用来仿真及测试逻辑门电路芯片。  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/Hardware%20Simulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/Hardware%20Simulator%20Tutorial.pdf)
CPU仿真器 | 仿真Hack计算机系统的运作，用来测试运行使用Hack机器语言编写的程序。<br/>（可运行二进制形式或汇编形式的版本）  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/CPU%20Emulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/CPU%20Emulator%20Tutorial.pdf)
VM仿真器 | 仿真书中描述的虚拟机的运作，用来运行测试使用VM语言编写的基于Hack-Jack平台的程序。  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/VM%20Emulator%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/VM%20Emulator%20Tutorial.pdf)
汇编编译器 | 将使用Hack汇编语言编写的程序翻译成Hack二进制机器代码。<br/>编译后的代码可以直接在硬件仿真器里的计算机芯片运行或在CPU仿真器上运行（速度更快）  | PPT[![PPT](http://www.nand2tetris.org/icons/powerpoint.gif)](http://www.nand2tetris.org/tutorials/PPT/Assembler%20Tutorial.ppt) PDF[![PDF](http://www.nand2tetris.org/icons/acrobat.gif)](http://www.nand2tetris.org/tutorials/PDF/Assembler%20Tutorial.pdf)
编译器 | 将使用Jack语言编写的程序编译成VM代码，该代码可在VM仿真器上执行。<br/>当然，VM代码还可以进一步由VM编译器和汇编编译器编译后生成机器代码在CPU仿真器或硬件仿真器上运行。  | 命令行程序
操作系统 | Jack OS扩展了Jack语言，就想Java标准类库扩展了Java语言一样。<br/>工具包中提供了两种实现：<br/>(1) 使用纯Jack余元编写的.vm类文件<br/>(2) 直接嵌入在VM仿真器里的更高效的版本  | ——
文本比较器 | 比较两个文本是否内容一致，可用来检查自己实现的软硬件是否正确，类似`diff`命令  | 命令行程序


详情请访问：<http://http://www.nand2tetris.org/software.php>

[`返回目录`](#content-table)
### [Projects](id:projects) 
（项目）
#### [[Project 01](https://github.com/ReionChan/nand2tetris/tree/master/projects/01)](id:project-01)
* 基本逻辑门 (1位)
	* [Not 非门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Not.hdl)
	
	* [And 与门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/And.hdl)
	
	* [Or 或门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or.hdl)
	
	* [Xor 异或门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Xor.hdl)
	
	* [Mux 2路复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux.hdl) [^3]
	
	* [DMux 解复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux.hdl) [^4]
	
	
* 多位 [^5] 基本门 (16位)
	* [Not16 16-位非门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Not16.hdl)
	
	* [And16 16-位与门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/And16.hdl)
	
	* [Or16 16-位或门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or16.hdl)
	
	* [Mux16 16-位2路复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux16.hdl)
	
	
* 多通道 [^6] 逻辑门
	* [Or8Way 8路或门](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Or8Way.hdl)
	
	* [Mux4Way16 4路16-位复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux4Way16.hdl)
	
	* [Mux8Way16 8路16-位复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/Mux8Way16.hdl)
	
	* [DMux4Way 4路解复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux4Way.hdl)
	
	* [DMux8Way 8路解复用器](https://github.com/ReionChan/nand2tetris/blob/master/projects/01/DMux8Way.hdl)

[`返回目录`](#content-table)
	
#### [[Project 02](https://github.com/ReionChan/nand2tetris/tree/master/projects/02)](id:project-02)

* 加法器
	* [HalfAdder 半加器](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/HalfAdder.hdl)
	
	* [FullAdder 全加器](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/FullAdder.hdl)
	
	* [Add16 16-位加法器](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Add16.hdl)
	
	* [Inc16 16-位增量器](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Inc16.hdl)
	
* ALU 算术逻辑单元
	* [Or16Way 16路或门](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/Or16Way.hdl)
	
	* [ALU 16-位算术逻辑单元](https://github.com/ReionChan/nand2tetris/blob/master/projects/02/ALU.hdl)

[`返回目录`](#content-table)
#### [[Project 03](https://github.com/ReionChan/nand2tetris/tree/master/projects/03)](id:project-03)

* [Bit 1-位寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/Bit.hdl)

* [Register 16-位寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/Register.hdl)

* [RAM8 8-寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/RAM8.hdl)

* [RAM64 64-寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/RAM64.hdl)

* [RAM512 512-寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM512.hdl)

* [RAM4K 4K-寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM4K.hdl)

* [RAM16K 16K-寄存器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/b/RAM16K.hdl)

* [PC 计数器](https://github.com/ReionChan/nand2tetris/blob/master/projects/03/a/PC.hdl)

[`返回目录`](#content-table)
#### [[Project 04](https://github.com/ReionChan/nand2tetris/tree/master/projects/04)](id:project-04)

* [Mult.asm 乘法程序](https://github.com/ReionChan/nand2tetris/blob/master/projects/04/mult/Mult.asm)

* [Fill.asm I/O处理程序](https://github.com/ReionChan/nand2tetris/blob/master/projects/04/fill/Fill.asm)

[`返回目录`](#content-table)
#### [[Project 05](https://github.com/ReionChan/nand2tetris/tree/master/projects/05)](id:project-05)

* [CPU 中央处理器](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/CPU.hdl)

* [Memory 内存](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/Memory.hdl)

* [Computer 计算机](https://github.com/ReionChan/nand2tetris/blob/master/projects/05/Computer.hdl)

[`返回目录`](#content-table)
#### [[Project 06](https://github.com/ReionChan/nand2tetris/tree/master/projects/06)](id:project-06)

* [汇编编译器源码](https://github.com/ReionChan/nand2tetris/tree/master/projects/06/Assembler)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Parser.java) 语法分析器
	
	* [Code.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Code.java) 汇编语言助记符译码器
	
	* [SymbolTable.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/SymbolTable.java) 符号表
	
	* [Assembler.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler/org/reion/Assembler.java) 汇编编译器入口类
	
* [汇编编译器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/Assembler.jar)

* 测试程序

	* 将常数2和3相加 [Add.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/add/Add.asm) 
	
	* 计算R0、R1的大值保存在R2
	
		* [MaxL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/max/MaxL.asm)（无符号版）
		
		* [Max.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/max/Max.asm)
		
	* 自屏幕左上角画一个矩形
		* [RectL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/rect/RectL.asm)（无符号版）
		
		* [Rect.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/rect/Rect.asm)
		
	* 单人乒乓游戏
	
		* [PongL.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/pong/PongL.asm)（无符号版）
		
		* [Pong.asm](https://github.com/ReionChan/nand2tetris/blob/master/projects/06/pong/Pong.asm)

[`返回目录`](#content-table)
#### [[Project 07](https://github.com/ReionChan/nand2tetris/tree/master/projects/07)](id:project-07)

* [VM翻译器](https://github.com/ReionChan/nand2tetris/tree/master/projects/07/VMTranslator)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/Parser.java) VM语法分析器
	
	* [CodeWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/CodeWriter.java) VM命令转汇编代码生成器
		
	* [VMTranslator.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator/org/reion/VMTranslator.java) VM翻译器入口类
	
* [VM翻译器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/VMTranslator.jar)

* 测试程序

	* 堆栈运算
		
		* 压入并相加两个常量 [SimpleAdd.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm)
		
		* 执行一系列堆栈上的运算、逻辑操作 [StackTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/StackArithmetic/StackTest/StackTest.vm)
			
	* 内存访问
		
		* 使用虚拟内存段执行pop和push操作 [BasicTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/BasicTest/BasicTest.vm)
			
		* 使用pointer段、this段、that段执行pop和push操作 [PointerTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/PointerTest/PointerTest.vm)
		
		* 使用static段执行pop和push操作 [StaticTest.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/07/MemoryAccess/StaticTest/StaticTest.vm)

[`返回目录`](#content-table)
#### [[Project 08](https://github.com/ReionChan/nand2tetris/tree/master/projects/08)](id:project-08)

* [VM翻译器](https://github.com/ReionChan/nand2tetris/tree/master/projects/08/VMTranslator)

	* [Parser.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/Parser.java) VM语法分析器
	
	* [CodeWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/CodeWriter.java) VM命令转汇编代码生成器
		
	* [VMTranslator.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator/org/reion/VMTranslator.java) VM翻译器入口类
	
* [VM翻译器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/VMTranslator.jar)

* 测试程序

	* 程序控制流命令测试程序
		
		* 计算1+2+...+n，结果压入堆栈 [BasicLoop.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/ProgramFlow/BasicLoop/BasicLoop.vm)
		
		* 斐波拉契数列 [FibonacciSeries.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm)
			
	* 函数调用命令测试程序
		
		* 调用简单的计算方法并返回结果 [SimpleFunction.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/SimpleFunction/SimpleFunction.vm)
			
		* 嵌套函数调用 [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/NestedCall/Sys.vm)
		
		* VM函数调用命令、引导程序和其他VM命令全面测试
			
			* [Main.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/FibonacciElement/Main.vm)
			
			* [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/FibonacciElement/Sys.vm)
		
		* VM实现对静态变量处理的全面测试
			
			* [Class1.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Class1.vm)
			
			* [Class2.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Class2.vm) 
			
			* [Sys.vm](https://github.com/ReionChan/nand2tetris/blob/master/projects/08/FunctionCalls/StaticsTest/Sys.vm)

[`返回目录`](#content-table)		
#### [[Project 09](https://github.com/ReionChan/nand2tetris/tree/master/projects/09)](id:project-09)

* [Hello](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/Hello) 打招呼程序 `Author: Reion Chan`

	![Hello_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/Hello/Hello.gif "Hello_demo")

* [Countdown Timer](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/CountdownTimer) 倒计时程序 `Author: Reion Chan`

	![Countdown_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/CountdownTimer/Countdown.gif "Countdown_demo")


* [Rolling Ball](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/RollingBall) 滚球程序 `Author: Reion Chan`
	
	![RollingBall_demo](https://raw.githubusercontent.com/ReionChan/nand2tetris/master/projects/09/RollingBall/RollingBall.gif "RollingBall_demo")
	
	[![YouTube](https://github.com/ReionChan/nand2tetris/blob/master/res/youtube.png?raw=true "Watch This Demo 查看此视频")](https://www.youtube.com/watch?v=demDX8JKg0c) [Watch Demo in YouTube 查看此视频](https://www.youtube.com/watch?v=demDX8JKg0c)

* [Square](https://github.com/ReionChan/nand2tetris/tree/master/projects/09/Square) 官方样例程序 `Author: Nand2Tetris.org`

	![Square_demo](https://github.com/ReionChan/nand2tetris/blob/master/projects/09/Square/Square.gif?raw=true "Square_demo")

* [Jack_OS_API.PDF](https://github.com/ReionChan/nand2tetris/blob/master/projects/09/Jack%20OS%20API.pdf)

[`返回目录`](#content-table)
#### [[Project 10](https://github.com/ReionChan/nand2tetris/tree/master/projects/10)](id:project-10)

* [Jack语法分析器](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/JackAnalyzer)

	* [CompilationEngine.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/CompilationEngine.java) 语法分析引擎
	
	* [JackTokenizer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/JackTokenizer.java) 字元转换器
	
	* [XmlUtils.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/XmlUtils.java) 语法树XML展现器
		
	* [JackAnalyzer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer/org/reion/JackAnalyzer.java) 语法分析器入口
	
* [Jack语法分析器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/10/JackAnalyzer.jar)

* 测试程序

	* [Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/Square) 交互小游戏
	
	* [Expressionless Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/ExpressionlessSquare) 无表达式交互小游戏
	
	* [Array Test](https://github.com/ReionChan/nand2tetris/tree/master/projects/10/ArrayTest) 数组测试程序

[`返回目录`](#content-table)
#### [[Project 11](https://github.com/ReionChan/nand2tetris/tree/master/projects/11)](id:project-11)

* [Jack编译器](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/JackCompiler)

	* [CompilationEngine.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/CompilationEngine.java) 语法分析引擎
	
	* [JackTokenizer.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/JackTokenizer.java) 字元转换器
	
	* [SymbolTabel.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/SymbolTabel.java) 符号表
	
	* [VMWriter.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/VMWriter.java) VM命令输出器
		
	* [JackCompiler.java](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler/org/reion/JackCompiler.java) 编译器入口
	
* [Jack编译器Java二进制jar文件](https://github.com/ReionChan/nand2tetris/blob/master/projects/11/JackCompiler.jar)

* 测试程序

	* [Seven](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Seven) 计算(3*2)+1的值 （常量、运算表达式、do语句、return语句）
	
	* [Convert To Bin](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/ConvertToBin) 十进制转二进制 （表达式、函数、语句）
	
	* [Square Dance](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Square) 交互游戏 （构造函数、方法、成员字段、方法调用）
	
	* [Average](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Average) 求平均值 （数组、字符串）
	
	* [Pong](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/Pong) 单人乒乓游戏 （对象、静态变量）
	
	* [Complex Arrays](https://github.com/ReionChan/nand2tetris/tree/master/projects/11/ComplexArrays) 复数计算 （数组引用、表达式）

[`返回目录`](#content-table)
#### [[Project 12](https://github.com/ReionChan/nand2tetris/tree/master/projects/12)](id:project-12)

* [Math.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Math.jack) 数学计算类
  
	* [MathTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/MathTest)测试程序
	
* [String.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/String.jack) 字符串类
  
	* [StringTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/StringTest)测试程序
	
* [Array.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Array.jack) 数组类
  
	* [ArrayTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/ArrayTest)测试程序
	
* [Output.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Output.jack) 字符输出处理类
  
	* [OutputTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/OutputTest)测试程序
	
* [Screen.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Screen.jack) 屏幕操作类 

	* [ScreenTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/ScreenTest)测试程序
	
* [Keyboard.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Keyboard.jack) 键盘操作类
  
	* [KeyboardTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/KeyboardTest)测试程序
	
* [Memory.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Memory.jack) 内存直接访问类
  
	* [MemoryTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/MemoryTest)测试程序
	
* [Sys.jack](https://github.com/ReionChan/nand2tetris/blob/master/projects/12/Sys.jack) 操作系统启动类
 
	* [SysTest](https://github.com/ReionChan/nand2tetris/tree/master/projects/12/SysTest)测试程序


[`返回目录`](#content-table)
### [Errata](id:errata) 
（中文版勘误）

* `4.2.3节` `Page-67` 
	图4.3中，表格右边的标题应为`（当a=1）comp助记符`
	
* `4.2.5节` `Page-70`
	第二段关于屏幕描述中，像素映射位置应为 `RAM[16384+r·32+c/16]`

* `8.2.2节` `Page-160`
	function f n 原文对n的解释位`该函数有n个参数`，应改为`该函数有n个本地变量`

* `11.3.4节` `Page-240`
	writeFunction中参数`nArgs(int)` 应改为 `nLocals(int)`

[`返回目录`](#content-table)
### [Copyright](id:copyright) 
（版权）

所有程序及文件（Nand2Tetris所包含的原始文件除外）版权归属Reion Chan。

如你使用本程序及文件请注明作者(Reion Chan)，详细参见[GNU GPLv3](./LICENSE)

未遵照许可或私自盗用此程序及文件将被诅咒一生只写漏洞百出的程序。


[`返回目录`](#content-table)
### [Licence](id:licence) 
（许可证）

本程序为自由软件：你可以在遵照[GNU GPLv3](./LICENSE)（及后续版本）条款的前提下，转发、修改本程序。

本程序旨在学习、交流，对本程序不承担任何担保责任，详细参见[GNU GPLv3](./LICENSE)许可。

本程序包含一个GUN 通用公共授权[GNU GPLv3](./LICENSE)。如果没有，请访问<http://www.gnu.org/licenses/>

[`返回目录`](#content-table)

### Footnotes  
（脚注）
	
[^1]: 周维、宋磊、陈曦翻译，电子工业出版社出版

[^2]: MIT Press, By [`Noam Nisan`](http://www.cs.huji.ac.il/~noam/) and [`Shimon Schocken`](http://shimonschocken.com/)

[^3]: [Multiplexor 多路复用器](https://en.wikipedia.org/wiki/Multiplexer)，即：多路模拟信号通过选择位确定哪一路进行输出，多路串行共享输出线路
	
[^4]: Demultiplexor 解复用器，与多路复用器相反，将单路模拟信号通过选择位确定输出到多条输出位的其中一条线路
	
[^5]: `多位` 输入、输出线路一次能够接纳、输出的比特位数
	
[^6]: `多通道` 输入线路的条数
