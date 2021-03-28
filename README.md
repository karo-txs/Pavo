# ![peacock(1)](https://user-images.githubusercontent.com/58193125/111029765-30858580-83dd-11eb-8626-ee9465888424.png) Pavo 
## _A minimalist compiler_
Work done for the Compilers course at the Universidade Católica de Pernambuco (UNICAP).

[![Doc](https://img.shields.io/static/v1?label=Documentation&message=1.0.0&color=blue&style=for-the-badge")](https://afkaro.github.io/Pavo/)

Students:
- Karolayne Teixeira da Silva ([AFKaro](https://github.com/AFKaro))
- Luiz Fernando Leite ([LuizFernando-o](https://github.com/LuizFernando-o))

> Project Status: In development :warning:

## Features (Functionalities)
### - Lexical Analysis:
- Token identification of programs with syntax similar to the C programming language;
- Personalized error messages.
### - General:
- Import .txt files to editor;
- Dark mode. 

## Technologies / Libraries Used 

- [JavaJRE] - used to run Java platform applications; 
- [Scene Builder] - generates FXML, an XML-based markup language that enables users to define an application's user interface, separately from the application logic;
- [JavaFX] - java-based multimedia software platform for creating and making rich Internet applications available;
- [RichTextFX] - provides a text area for JavaFX with API to style ranges of text. It is intended as a base for rich-text editors and code editors with syntax highlighting.

## Accepted Tokens 

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

## Program Preview
![PavoReview](https://user-images.githubusercontent.com/58193125/111394529-a87fd400-8699-11eb-8cc5-14f40b43cd29.gif)

## Automatons
Automatons representing the lexical analyzer [[Slide](https://github.com/AFKaro/Pavo/tree/main/docs)].


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [JavaJRE]: <https://www.java.com/pt-BR/download/manual.jsp>
   [Scene Builder]: <https://gluonhq.com/products/scene-builder/>
   [JavaFX]: <https://openjfx.io/>
   [RichTextFX]: <https://github.com/FXMisc/RichTextFX>

