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

Inoltre, dato che i mini-giochi "matematici" (*Fast Calc, Count Words* e *Colored Count*) hanno la stessa modalità
di generazione della domanda e validazione della risposta, è stato creato un trait *MathMiniGameLogic* che viene
usato tramite *mixin*.

```
trait MathMiniGameLogic[Q <: Question]:
  self: MiniGameLogic[Q, Int, Boolean] =>

  val lastQuestion: Option[Q]

  protected def difficultyStep: Int

  protected def correctAnswer(question: Q): Int

  protected def withNewQuestion(question: Q): MiniGameLogic[Q, Int, Boolean]

  protected def advance(question: Q): (MiniGameLogic[Q, Int, Boolean], Q) =
    (
      withNewQuestion(question),
      question
    )

  override def parseAnswer(answer: String): Option[Int] = answer.trim.toIntOption

  override def validateAnswer(answer: Int): Boolean = lastQuestion match
    case Some(q) => answer == correctAnswer(q)
    case _ => false
```