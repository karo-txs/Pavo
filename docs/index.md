# Theoretical Documentation
## Abstract
This document aims to show the theoretical framework as well as the basis behind the Pavo compiler.
*******
## Index
 1. [Lexical Analyzer](#analisador-lexico)
 2. [Syntactic Analyzer](#analisador-sintatico)

*******
<div id='analisador-lexico'/>  

## 1. Lexical Analyzer

### 1.1. Scanner
The lexical analyzer, or scanner as it is also called, scans the source program character by character and translates it into a sequence of lexical symbols or tokens. It is in this phase that the reserved words, constants, identifiers and other words that belong to the programming language are recognized. The lexical analyzer performs other tasks such as the treatment of spaces, elimination of comments, counting the number of lines that the program has and so on.

### 1.2. Tokens
Below the list of tokens accepted by the lexical analysis.

| Expressions | Token Type | Expressions| Token Type |
| ------ | ------ | ------ | ------
| [a-z] or [A-Z]  | letter | <= | relational operator less equal|
| [0-9] | digit | ! | relational operator not |
| digit+ | int | != | relational operator not equal |
| digit+.digit+ | float | == | relational operator equal | 
| 'letter' | char | ( | special character parentheses open |
| "anything" | char sequence | ) | special character parentheses closed |
| + | arithmetic operator plus | { | special character braces open |
| - | arithmetic operator minus | } | special character braces closed |
| * | arithmetic operator multiplication| [ | special character brackets open |
| ^ | arithmetic operator power | ] | special character brackets closed |     
| / | arithmetic operator division | # | special character hash |           
| = | arithmetic operator assign | , | special character comma |   
| > | relational operator more | ; | special character semicolon |     
| < | relational operator less | : | special character two points |
| >= | relational operator more equal | && | logical operator and |
| ll | logical operator or |

### 1.3. Máquinas de estado

![image](https://user-images.githubusercontent.com/70172712/116269896-000f6600-a755-11eb-9324-fc5c98009c0c.png)
![image](https://user-images.githubusercontent.com/70172712/116270009-19b0ad80-a755-11eb-8c8a-8d2289c58da4.png)
![image](https://user-images.githubusercontent.com/70172712/116270527-8e83e780-a755-11eb-9a7c-f725ac4e26e5.png)
![image](https://user-images.githubusercontent.com/70172712/116270704-b5dab480-a755-11eb-8a97-09cff8a9aa32.png)
![image](https://user-images.githubusercontent.com/70172712/116270793-ca1eb180-a755-11eb-988e-4ec794bea1d0.png)
![image](https://user-images.githubusercontent.com/70172712/116270886-e3bff900-a755-11eb-8db7-23bbe7844b4f.png)
![image](https://user-images.githubusercontent.com/70172712/116270944-f1757e80-a755-11eb-9982-4bbdfaec02f1.png)
![image](https://user-images.githubusercontent.com/70172712/116271029-00f4c780-a756-11eb-88bd-f8e951368a22.png)
![image](https://user-images.githubusercontent.com/70172712/116271108-0fdb7a00-a756-11eb-9060-5560a3e86db0.png)
![image](https://user-images.githubusercontent.com/70172712/116271168-1cf86900-a756-11eb-8231-84079af6e17b.png)
![image](https://user-images.githubusercontent.com/70172712/116271233-2d104880-a756-11eb-886e-20c53073cbaf.png)
![image](https://user-images.githubusercontent.com/70172712/116271396-53ce7f00-a756-11eb-9f9e-79482892a6d3.png)

*******
<div id='analisador-sintatico'/>  

## 2. Syntactic Analyzer
### 2.1. Parser
The parser, also known as parser, has the main task of determining whether the input program represented by the token flow has the valid sentences for the programming language.
### 2.2. Context-Free Grammars (CFG)

<program> ::= int main "(" ")" <block>
<block> ::= "{" (<statement_var>)* {<command>}* "}"
<command> ::= <basic_command>
            | <while_>
            | <do_while>
            | <for_>
            | <if_>    
<command_> ::= <basic_command>
            | <if_>           
<basic_command> ::= <assignment>
            | <block>
<while> ::= while "("<relational_exp>")" <command>
<assignment> ::= <id> "=" <arithmetic_exp> ";"
<assignment_> ::= <id> "=" <arithmetic_exp>
<relational_exp> ::= <arithmetic_exp> <relational_operator> <arithmetic_exp>
<arithmetic_exp> ::= <term>  <arithmetic_exp_>
<arithmetic_exp_> ::= "+" <term> <arithmetic_exp_>
            | "-" <term> <arithmetic_exp>
            | ε
<term> ::= <factor> <term_>
<term_> ::=  "*" <factor> <term_>
 	          | "/" <factor><term_>
            | ε
<factor> ::= "(" <arithmetic_exp> ")"
            | <id>
            | <float>
            | <integer>
            | <char>
<logical_exp> ::= <relational_exp> <logical> 
<logical> ::= "||" <relational_exp>
            | "&&" <relational_exp>
            | ε
<statement_var> ::= <type> <id> ";"
<type> ::= int | float | char
<if_> ::= if "("<relational_exp>")" <command> (else <command>)*
<for_> ::= for "("<assignment_> ";" <relational_exp>  ";" <assignment_>  ")" <command>
<do_while> ::= do <command> while "(" <logical_exp> ")" ";"
<print>::= "(" <print_> ")" ";"
<print_> ::= <char_sequence> 
		          | <arithmetic_exp>
            | <logical_exp>
