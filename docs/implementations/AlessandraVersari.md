# Alessandra Versari
Nel corso del processo di sviluppo mi sono occupata dei seguenti componenti: 
* Logica e test del mini gioco Fast Calc
* Logica e test del mini gioco Word Memory 
* GUI

## Fast Calc e Word Memory 
Durante il secondo e il terzo sprint mi sono dedicata allo sviluppo dei due mini giochi Fast Calc e Word Memory. 
Nel gioco Fast Calc l'obiettivo principale era quello di creare un gioco basato su espressioni matematiche, ma comunque rispettando le caratteristiche base del gioco a 
cui ci siamo ispirati: infatti ho scelto di sviluppare la logica di calcolo in modo che fosse 
in parte estendibile, ma in modo limitato in quanto il gioco prevede sfide semplici, che l'utente possa svolgere a mente e piuttosto rapidamente. 
Il gioco infatti si basa principalmente su espressioni generate in modo casuale scegliendo operatori e numeri  da utilizzare in base al livello di difficoltà corrente. 
E' possibile configurare il numero di cifre utilizzabili, ma abbiamo ritenuto sufficiente utilizzare una sola cifra e solo gli operatori somma, sottrazione e moltiplicazione, 
in quanto eventuali operatori o elementi aggiuntivi avreebbero reso le espressioni troppo complicate per lo scopo del gioco. 

Uno dei costrutti avanzati della programmazione funzionale che ho utilizzato con maggiore frequenza è il for - yield, 
siccome permette di rendere porzioni di codice molto più leggibili in modo semplice. 

    `
    private def buildExpression(
    numbers: List[String],
    operators: List[String]
    ): List[String] =
    for
    (n, op) <- numbers.zipAll(operators, "", "")
    token   <- List(n, op) if token.nonEmpty
    yield token
    `

Nel secondo mini gioco che ho implementato, Word Memory, l'obiettivo era generare una lista di parole di lunghezza proporzionale all'aumento della difficoltà. 
In questo caso l'utente può osservare le parole per 10 secondi, dopo di che la lista verrà nascosta e dovrà digitare tutte le parole che riesce a ricordare.
Il metodo che controlla la correttezza della risposta, considera solo le parole scritte correttamente, indipendentemente dall'ordine e restituisce un double da 0.0 a 1.0 che indica la percentuale di correttezza della domanda. 
Abbiamo scelto di considerare corrette tutte le risposte che ottengono un punteggio superiore alle 0.6.

In WordMemoryLogic ho utilizzato un extension method per il tipo String, in modo da evitare la ripetizione del codice e renderlo più leggibile. Inoltre nel codice sotto si nota che ho utilizzato anche il costrutto 
fold che mi ha permesso di lavorare in modo più semplice e veloce con gli Option.

    `extension (s: String) private def toWordSet: Set[String] = s.split(" ").filter(_.nonEmpty).toSet
    
    override def validateAnswer(answer: String): Double =
    lastQuestion.fold(0.0)(question =>
    val expectedWordsNumber = question.text.toWordSet
    val answerWordsNumber   = answer.toWordSet
    answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
    )`

## GUI 
[Torna indietro](../Implementazione.md)