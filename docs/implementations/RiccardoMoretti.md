# Riccardo Moretti

Nel corso dello sviluppo mi sono occupato principalmente dell'implementazione delle seguenti *features*:

- Test e logica del mini-gioco *Right Directions*.
- Gestione dell'input. Principalmente implementato per il mini-gioco *Right Directions* è generalizzato per permetterne l' utilizzo in future implementazioni.

Per iniziare riporto la struttura della parte legata al minigioco Right Directions:
```
-rightDirections
    -structure
        -treeLogic
            BinaryTree.scala
            Leaf.scala
            Node.scala
        DirectionsTreeBuilder.scala
        EvaluateOperation.scala
        OperationBuilder.scala
        Token.scala
    RightDirectionsLogic.scala
```

Dove il *-* indica una cartella.

---

La cartella più interna chiamata treeLogic è la parte legata alla struttura dati, un albero binario, con
cui le domande per i minigiochi vengono create e valutate per ottenere una risposta, è diviso in un trait BinaryTree.scala
che viene implementato dai file Leaf.scala e Node.scala che, rispettivamente, rappresentano foglie e nodi dell'albero.

```
trait BinaryTree[A]:

  val value: A
  val depth: Int
  val left: Option[BinaryTree[A]]
  val right: Option[BinaryTree[A]]
  
    def expand(target: A, newValue: A, leftValue: Option[A], rightValue: Option[A])
    : BinaryTree[A]
    
    def toString: String
    
    def contains(value: A): Boolean
```

Gli elementi di un albero binario devono supportare tre operazioni principali:

- Espandere l'albero cercando un valore *target* da sostituire, che viene sostituito con
  *newValue* e, nel caso di operazioni binarie o unarie va ad aggiungere
  foglie sinistre/destre. Esistono anche operazioni che effettuano solo
  sostituzioni inplace, il processo è da intendere come quello che avverrebbe
  ad una context free grammar, dunque ci saranno Token, che vedremo più tardi,
  generici che possono essere sostituiti da Token più specifici.
- Constatare la presenza o meno di un valore all'interno dell' albero.
- Trasformare in stringa l' albero per poterlo mostrare come
  domanda testuale e, allo stesso tempo, avere una struttura regolare per
  poter calcolare il risultato della domanda a partire dalla
  stessa.

Il contenuto di leaf.scala verrà omesso in quanto le operazioni sono facilmente
interpretabili, generalmente le implementazioni sono poco più di un metodo
getter specializzato.
Per quanto riguarda Node.scala le principali implementazioni sono state riassunte
qui di seguito:

la funzione expand lavora ricorsivamente come segue: se ci troviamo alla foglia
contenente *target* allora restituiamo un nuovo nodo che contiene *newValue* come
valore del nodo e l' intero albero come ramo sinistro.
In caso contrario vado ad espandere il ramo sinistro (che in un nodo è sempre presente)
e quello destro solo se presente, in caso siano entrambi presenti viene accettata una
qualsiasi espansione, altrimenti l'unica scelta possibile è accettare quella sinistra.
```
override def expand( target: A, newValue: A, leftValue: Option[A],
      rightValue: Option[A]): BinaryTree[A] =
  if value == target then new Node(newValue, this, None)
  else
    val expandedLeft = left.get.expand(target, newValue, leftValue, rightValue)
     if (right.isDefined)
       val expandedRight = right.get.expand(target, newValue, leftValue, rightValue)
       expandBothBranchNode(expandedLeft, expandedRight)
     else Node(value, expandedLeft, None)
```

Le altre due funzioni lavorano in sinergia con le foglie appartenenti al nodo,
se presenti.

```
override def contains(value: A): Boolean =
this.value == value || left.exists(_.contains(value)) || right.exists(_.contains(value))

override def toString: String =
val leftStr  = left.fold("")(_.toString)
val rightStr = right.fold("")(_.toString)
(leftStr, rightStr) match
case ("", "") => value.toString
case (_, "")  => s"($value $leftStr)"
case _        => s"($leftStr $value $rightStr)"
```

---

La seconda cartella più profonda, structure, contiene gli elementi
utili alla costruzione degli alberi adattati a Token, che è un enum contenente
tutti gli elementi che possono apparire all' interno di una operazione
del minigioco corrente. Token è accoppiato con un companion object.

Da specificare che la logica che segue si basa sull' idea che ogni Token
ha un valore di complessità assegnato, questo perchè l' aspettativa è di
avere quesiti di difficoltà incrementale, dunque sarà possibile specificare
una complessità obiettivo a cui il quesito cercherà di avvicinarsi con la
scelta dei Token.

Sempre dentro structure troviamo un trait OperationBuilder.scala che in
maniera generica implementa la costruzione astratta di alberi e la sua
specializzazione DirectionsTreeBuilder.scala. Infine EvaluateOperation.scala
viene impiegato per calcolare le potenziali risposte corrette di una stringa
contenente un albero a cui sia stato applicato la funzione .toString.

Di seguito un estratto di DirectionsTreeBuilder.scala:
```
val TokensNextComplexity = (treeComplexity, complexity) match
          case (0, c) if c >= BINARY_OPERATOR_COMPLEXITY => BINARY_OPERATOR_COMPLEXITY
          case (c1, c2) if c1 != c2                      => UNARY_OPERATOR_COMPLEXITY
          case _                                         => SUBSTITUTE_OPERATOR_COMPLEXITY
val tokenToAdd           = Token.randomOperatorUpTo(TokensNextComplexity)
expandTree(
  tokenToAdd.complexity match
    case BINARY_OPERATOR_COMPLEXITY     => root.expand(X, tokenToAdd, Some(X), Some(X))
    case UNARY_OPERATOR_COMPLEXITY      => root.expand(X, tokenToAdd, Some(X), None)
    case SUBSTITUTE_OPERATOR_COMPLEXITY => root.expand(X, tokenToAdd, None, None)
    case _                              => root
  ,
  complexity
)
```
La parte più importante da sottolineare, evidenziata dal frammento di codice sopra
è la divisione logica delle tre operazioni principali legate alla struttura
dati che concedono gli alberi binari: le uniche trasformazioni possibili sono quelle
binarie, come X => X And X, quelle unarie come X => Not X oppure quelle di
sostituzione come X => Left

Per quanto riguarda il file Token.scala le principali peculiarità riguardano
la gestione della complessità, che è già stata introdotta in precedenza.
Ogni Token ha una propria complessità, necessariamente 0, 1 o 2 per operazioni
sostitutive, unarie e binarie

```
enum Token(val complexity: Int):
  case And   extends Token(2)
  case Or    extends Token(2)
  case Not   extends Token(1)
  case X     extends Token(0)
  [...]
  
  override def toString: String = this match
    case Token.And   => "and"
    case Token.Or    => "or"
    case Token.Not   => "not"
    case Token.X     => "x"
    [...]
```
Il companion object, oltre a definire alcuni val per categorizzare i valori,
provvede anche alcune operazioni utili per manipolare l' enum:
```
object Token:
  val operators: Seq[Token]  = Seq(And, Or, Not)
  val directions: Seq[Token] = Seq(Up, Left, Right, Down)
  val others: Seq[Token]     = Seq(X, Empty, LP, RP)
  val all: Seq[Token]        = operators concat directions concat others
  
  def randomOperatorUpTo(maxComplexity: Int): Token =
    val complexityToUse =
      if maxComplexity >= BINARY_OPERATOR_COMPLEXITY then 
        Random.nextInt(BINARY_OPERATOR_COMPLEXITY) + 1
      else maxComplexity

    val filtered = Random
      .shuffle(operators concat directions)
      .filter(_.complexity <= complexityToUse)
    filtered.maxBy(_.complexity)
      
  def fromString(str: String): Token =
    all.find(_.toString.equals(str.trim)).getOrElse(Empty)
```

L'ultimo file importante è EvaluateOperation.scala che, quando riceve una stringa,
restituisce una lista con tutti i simboli che saranno considerati corretti se
inseriti come risposta dall' utente.
Per semplificare l' esperienza di gioco solo un And oppure solo un Or possono apparire,
ho notato che qualora non sia così le domande diventano molto intricate e difficili da
interpretare, offuscando invece di rendere più complessa la domanda, il codice che segue
riflette questa scelta implementativa.

```
object EvaluateOperation:
  private def stripParentheses(s: String): String =
    s.replace("(", "").replace(")", "").trim

  def evaluateOperationFromString(input: String, currentList: Seq[Token]): Seq[Token] =
    val trimmedInput = stripParentheses(input)
    trimmedInput match
      case operation if operation.contains("and")                               =>
        val Array(left, right) = operation.split("and")
        combineAnd(left, right)
      case operation if operation.contains("or")                                =>
        val Array(left, right) = operation.split("or")
        combineOr(left, right)
      case operation if !(operation.contains("x") || operation.contains("not")) =>
        Seq(Token.fromString(stripParentheses(operation)))
      case operation                                                            =>
        handleNotCondition(operation)
        
  private def combineAnd(left: String, right: String): Seq[Token] =
  (evaluateOperationFromString(left, Nil) intersect
    evaluateOperationFromString(right, Nil)).distinct

  private def combineOr(left: String, right: String): Seq[Token] =
    (evaluateOperationFromString(left, Nil) concat
      evaluateOperationFromString(right, Nil)).distinct
```

