# Team

In questo capitolo vengono riportate tutte le implementazioni fatte in gruppo, le quali sono poi state utilizzate
da tutti i membri del team.

## Logica dei mini-games

Ogni mini-gioco implementato nel progetto deve aderire al contratto *MiniGameLogic* il quale prevede quattro funzioni
principali da implementare:
- generare nuove domande
- fare il *parsing* delle risposte dell'utente
- validare le risposte dell'utente
- controllare se il mini-gioco è finito

Dato che i mini-giochi possono generare diversi tipi di domande, accettare diversi tipi di input e produrre diversi
tipi di risposta, il trait è stato implementato tramite l'uso dei *generics*. Inoltre, per garantire
l'immutabilità, ogni volta che viene generata una nuova domanda deve essere restituita anche una copia dello stato
aggiornato della logica.

```
trait MiniGameLogic[Q <: Question, A, B]:
  
  def generateQuestion: (MiniGameLogic[Q, A, B], Q)
  
  def parseAnswer(answer: String): Option[A]

  def validateAnswer(answer: A): B

  def isMiniGameFinished: Boolean
```

In questo caso, *A* e *B* sono due tipi generici che possono essere di qualsiasi tipo mentre *Q* (che rappresenta
il tipo di domanda) deve essere un sotto tipo del trait *Question*.

```
sealed trait Question

case class SimpleTextQuestion(text: String) extends Question

case class ColoredCountQuestion(
  numbersWithColor: Seq[(Int, ColoredCountColors)],
  colorRequired: ColoredCountColors
) extends Question
```