# Documentação Teórica
## Resumo
Esse documento tem como objetivo mostrar o referencial teórico bem como a base por trás do compilador Pavo.
*******
## Índice
 1. [Analisador Lexico](#analisador-lexico)
 2. [Analisador Sintatico](#analisador-sintatico)

*******
<div id='analisador-lexico'/>  

## 1. Analisador Lexico

### 1.1. Scanner

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
| >= | relational operator more equal | & | special character address |

### 1.3. Máquinas de estado

*******
<div id='analisador-sintatico'/>  

## 2. Analisador Sintatico
### 2.1. Parser
### 2.2. Gramaticas Livres de Contexto (GLC)
### 2.3. Diagramas
