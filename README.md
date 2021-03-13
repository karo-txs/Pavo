# ![peacock(1)](https://user-images.githubusercontent.com/58193125/111029765-30858580-83dd-11eb-8626-ee9465888424.png) Pavo 
## _A minimalist compiler_
Work done for the Compilers course at the Universidade Católica de Pernambuco (UNICAP) 

Students:
- Karolayne Teixeira da Silva ([AFKaro](https://github.com/AFKaro))
- Luiz Fernando Leite ([LuisFernando-o](https://github.com/LuizFernando-o))

## Features (Functionalities)
### - Lexica Analysis:
- Generation of program tokens with C-like syntax;
- Personalized error messages.
### - General:
- Import .txt files to editor;
- Dark mode. 
- 
## Technologies / Libraries Used 

- [JavaJRE] - HTML enhanced for web apps!
- [Scene Builder] - awesome web-based text editor
- [JavaFX] - Markdown parser done right. Fast and easy to extend.

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

## License

MIT

**Free Software!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [JavaJRE]: <https://www.java.com/pt-BR/download/manual.jsp>
   [Scene Builder]: <https://gluonhq.com/products/scene-builder/>
   [JavaFX]: <https://openjfx.io/>

