# Simulação de FILAS.
---


### Requisitos:
- Java >= 8

- Caso seja java maior que o 8, deve ser recompilar o projeto.
```bash 
./gradlew build
```
isso vai gerar o .jar.

### Como rodar:
```bash
java -jar queueSim.jar
```
isso ira mostrar o help do projeto.

```bash
java -jar queueSim.jar model.
```
Vai rodar o arquivo modelo de exemplo localizado em: [model.json](model.json)
```bash
java -jar queueSim.jar --file=<file.json>.
```
Roda o arquivo json especficado pelo caminho.

### Estrutura JSON:
[modelExplained.jsonc](modelExplained.jsonc)