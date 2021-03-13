# ![peacock(1)](https://user-images.githubusercontent.com/58193125/111029765-30858580-83dd-11eb-8626-ee9465888424.png) Pavo 
## _A minimalist compiler_
Work done for the Compilers course at the Universidade Católica de Pernambuco (UNICAP).

Students:
- Karolayne Teixeira da Silva ([AFKaro](https://github.com/AFKaro))
- Luiz Fernando Leite ([LuisFernando-o](https://github.com/LuizFernando-o))

> Status do Projeto: Em desenvolvimento :warning:

## Features (Functionalities)
### - Lexica Analysis:
- Token identification of programs with syntax similar to the C programming language ;
- Personalized error messages.
### - General:
- Import .txt files to editor
- Dark mode. 

## Technologies / Libraries Used 

- [JavaJRE] - used to run Java platform applications 
- [Scene Builder] - generates FXML, an XML-based markup language that enables users to define an application's user interface, separately from the application logic.
- [JavaFX] - java-based multimedia software platform for creating and making rich Internet applications available.

## Accepted Tokens 

Below the list of tokens accepted by the lexical analysis.

| Expressions | Token Type | Expressions| Token Type |
| ------ | ------ | ------ | ------
| [a-z] or [A-Z] | letter |  >= | relational operator _ |
| [0-9] | digit | <= | relational operator _ |
| digit+ | int | ! | relational operator not |
| digit+.digit+ | float | != | relational operator not equal |
| 'letter' | char | == | relational operator equal |
| "[anyCharacter]*"| string | ( or ) | special character _ |
| . | punctuation | { or } | special character _ |
| + | arithmetic operator plus | [ or ] | special character _ |
| - | arithmetic operator minus | # | special character hash |
| * | arithmetic operator multiplication | , | special character _ |
| ^ | arithmetic operator power | ; | special character _ |
| / | arithmetic operator division | : | special character _ |
| = | arithmetic operator assign |
| > | relational operator _ |
| < | relational operator _ |

## Automaton
Automaton representing the lexical analyzer.
![PavoAutomatons](https://user-images.githubusercontent.com/58193125/111038152-32177380-8406-11eb-939e-ac937a65fb86.gif)
- ([Slide](https://github.com/AFKaro/Pavo/tree/main/docs))



[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [JavaJRE]: <https://www.java.com/pt-BR/download/manual.jsp>
   [Scene Builder]: <https://gluonhq.com/products/scene-builder/>
   [JavaFX]: <https://openjfx.io/>

