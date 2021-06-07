# ![peacock(1)](https://user-images.githubusercontent.com/58193125/111029765-30858580-83dd-11eb-8626-ee9465888424.png) Pavo 
## _A minimalist compiler_
Work done for the Compilers course at the Universidade Católica de Pernambuco (UNICAP).

[![Doc](https://img.shields.io/static/v1?label=Documentation&message=1.0.0&color=blue&style=for-the-badge")](https://afkaro.github.io/Pavo/)

Students:
- Karolayne Teixeira da Silva ([AFKaro](https://github.com/AFKaro))
- Luiz Fernando Leite ([LuizFernando-o](https://github.com/LuizFernando-o))

> Project Status: In development :warning:

## Features (Functionalities)
### ![lexica](https://user-images.githubusercontent.com/70172712/120956577-3663dc00-c72a-11eb-99fa-70c4e3b59a0c.png) Lexical Analysis 
- Token identification of programs with syntax similar to the C programming language;
- Personalized error messages.
### ![sintatica](https://user-images.githubusercontent.com/70172712/120956626-5398aa80-c72a-11eb-8442-8a1aa082bcf9.png) Syntactic Analyzer
- Determines whether the input program represented by the token stream has valid sentences for the programming language with syntax similar to C.
- Personalized error messages.
### ![sintatica](https://user-images.githubusercontent.com/70172712/120956626-5398aa80-c72a-11eb-8442-8a1aa082bcf9.png) Semantic Analyzer
- Type compatibility (assignment, instance and operations) and scope of variables and methods.
- Personalized error messages.

## Technologies / Libraries Used 

- [JavaJRE] - used to run Java platform applications; 
- [Scene Builder] - generates FXML, an XML-based markup language that enables users to define an application's user interface, separately from the application logic;
- [JavaFX] - java-based multimedia software platform for creating and making rich Internet applications available;
- [RichTextFX] - provides a text area for JavaFX with API to style ranges of text. It is intended as a base for rich-text editors and code editors with syntax highlighting.

## How to run by terminal 
```
cd /dist
java -jar pavo.jar
```

## Program Preview
![pavoScreen](https://user-images.githubusercontent.com/70172712/116619461-d9009200-a916-11eb-9b68-7cfe93190cb6.gif)

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [JavaJRE]: <https://www.java.com/pt-BR/download/manual.jsp>
   [Scene Builder]: <https://gluonhq.com/products/scene-builder/>
   [JavaFX]: <https://openjfx.io/>
   [RichTextFX]: <https://github.com/FXMisc/RichTextFX>

