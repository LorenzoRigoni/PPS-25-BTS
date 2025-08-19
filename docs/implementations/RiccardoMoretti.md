# Riccardo Moretti

Nel corso dello sviluppo mi sono occupato principalmente dell'implementazione delle seguenti *features*:

- Test e logica del mini-gioco *Right Directions*.
- Gestione dell' input. Principalmente implementato per il mini-gioco *Right Directions* è generalizzato per permetterne l' utilizzo in future implementazioni.

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

La cartella più interna chiamata treeLogic è la parte legata alla struttura dati, un albero binario, con 
cui le domande per i minigiochi vengono create e valutate per ottenere una risposta, è diviso in un trait BinaryTree.scala
che viene usato dai file Leaf.scala e Node.scala che, rispettivamente, rappresentano foglie e nodi dell' albero.

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

Le tre operazioni fondamentali che gli elementi dell' albero binario 
dovranno fare saranno: 

- Espandere l'albero cercando un valore *target* da sostituire, sostituito con
*newValue* e, nel caso di operazioni binarie o unarie va ad aggiungere 
foglie sinistre/destre. Esistono anche operazioni che effettuano solo 
sostituzioni inplace, il processo è da intendere come quello ceh avverrebbe
ad una context free grammar, dunque ci saranno Token, che vedremo più tardi,
generici che possono essere sostituiti da Token più specifici.
- Constatare la presenza o meno di un valore all' interno dell' albero.
- Trasformare in stringa l' albero per poterlo mostrare come
domanda testuale e, allo stesso tempo, avere una struttura regolare per
poter calcolare il risultato della domanda a partire dalla
stessa.

Il contenuto di node.scala verrà omesso in quanto le operazioni sono facilemnte
interpretabili, generalmente le implementazioni sono poco più di un metodo 
getter specializzato.
Per quanto riguarda Node.scala le principali implemetazioni sono state riassunte 
qui di seguito:

la funzione expand lavora ricorsivamente come segue: se ci troviamo alla foglia 
contentente *target* allora restituiamo un nuovo nodo contenente *newValue* come 
valore del nodo e l' intero albero come ramo sinistro.
In caso contrario vado ad espandere il ramo sinistro (che in un nodo è sempre presente)
e quello destro solo se presente, in caso siano entrambi presenti viene accettata una
qualsiasi espansione, altrimenti l' unica scelta possibile è accettare quella sinistra.
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

Le altre due funzioni lavorano in unisono con le foglie appartenenti al nodo,
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

La seconda cartella più profonda, structure, contiente gli elementi
utili alla costruzione degli alberi adattati a Token, un enum contenente
tutti gli elementi che possono apparire all' interno di una operazione 
del minigioco corrente. Token è accoppiato con un companion object.

Sempre dentro structure troviamo un trait OperationBuilder.scala che in
maniera generica implementa la costruzione astratta di alberi e la sua 
specializzazione DirectionsTreeBuilder.scala. Infine EvaluateOperation.scala 
viene impiegato per calcolare le potenziali risposte corrette di una stringa
contenente un albero a cui sia stato applicato la funzione .toString.

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
è la divisione logica delle tre operazioni principali concesse dalla struttura
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