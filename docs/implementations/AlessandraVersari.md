# Alessandra Versari
Nel corso del processo di sviluppo mi sono occupata dei seguenti componenti: 
* Logica e test del mini gioco Fast Calc
* Logica e test del mini gioco Word Memory 
* GUI
I primi due punti sono stati svolti prevalentemente in autonomia, mentre il terzo punto è stato sviluppato in
collaborazione con il collega Lorenzo Rigoni.

## Fast Calc e Word Memory 
Durante il secondo e il terzo sprint mi sono dedicata allo sviluppo dei due mini giochi Fast Calc e Word Memory.
Nel gioco Fast Calc l'obiettivo principale era quello di realizzare un sistema che generasse espressioni matematiche semplici,
mantenendo però un livello di difficoltà crescente.
Per rispettare lo scopo didattico e di allenamento mentale, ho scelto di limitare gli operatori disponibili a somma, 
sottrazione e moltiplicazione, e utilizzare soltanto numeri a una cifra. In questo modo le espressioni risultano 
abbastanza varie ma non troppo complesse, così da poter essere risolte mentalmente in tempi brevi.
Un aspetto importante è stato rendere la logica parzialmente estendibile, ma senza introdurre un livello di generalizzazione
eccessivo che avrebbe complicato inutilmente il codice. Ad esempio, la selezione degli operatori varia automaticamente in base al livello di difficoltà, 
tramite pattern matching:

    private def getOperatorsForDifficultyLevel(difficulty: Int): Seq[String] =
    difficulty match
    case d if d < NUM_SIMPLE_ROUNDS => Seq("+")
    case d if d < NUM_MEDIUM_ROUNDS => Seq("+", "-")
    case _                          => Seq("+", "-", "*")

Un costrutto di programmazione funzionale avanzata che ho usato con frequenza è il **for–yield**, che semplifica molto 
la generazione di strutture dati e rende semplifica molto la lettura del codice. 

    private def buildExpression(
    numbers: List[String],
    operators: List[String]
    ): List[String] =
    for
    (n, op) <- numbers.zipAll(operators, "", "")
    token   <- List(n, op) if token.nonEmpty
    yield token

Nel mini-gioco Word Memory l’obiettivo era generare una lista di parole casuali, la cui lunghezza cresce con la difficoltà.
L’utente ha 10 secondi per memorizzarle, dopodiché deve riscriverle.
La verifica della risposta non considera l’ordine ma solo la correttezza delle parole digitate, restituendo un punteggio 
Double tra 0.0 e 1.0. Per semplicità, consideriamo corretta una risposta con punteggio superiore a 0.6.

In questa porzione di logica ho utilizzato due costrutti Scala significativi:
* **Extension methods**, per aggiungere funzionalità a String senza ereditarietà. In particolare il seguente metodo consente di 
convertire rapidamente una stringa in un set di parole, evitando duplicazioni di codice.

      extension (s: String) private def toWordSet: Set[String] = s.split(" ").filter(_.nonEmpty).toSet
    

* **fold** sugli Option in modo da gestire il caso in cui non ci sia una domanda precedente (lastQuestion), ma evitando l’uso
di if-else o controlli manuali su None, ottenendo un codice più compatto e funzionale.
   
        override def validateAnswer(answer: String): Double =
        lastQuestion.fold(0.0)(question =>
        val expectedWordsNumber = question.text.toWordSet
        val answerWordsNumber   = answer.toWordSet
        answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
        )

## GUI 
Il design e la struttura del codice relativo alle view sono stati già esaminati nel capitolo precedente, evidenziando 
i vari pattern utilizzati.
Essendo un gioco che utilizza una UI relativamente semplice, ma composta da diversi panel simili tra loro, lo sviluppo 
si è concentrato sul riutilizzo del codice, cercando di rispettare al massimo il DRY principle.
Le classi principali che favoriscono il riutilizzo del codice sono _UIHelper_, che contiene una serie di metodi utili 
per comporre la GUI, e _SimpleAnswerQuestionGamePanel_, un trait che racchiude tutto il codice comune ai pannelli di gioco
che richiedono solo un titolo, una domanda e un campo per inserire la risposta.

Per semplificare la creazione dei bottoni, è stato creato il metodo createStyledButton, che oltre a impostare colore, 
font, dimensioni ecc., definisce anche l’event handler da eseguire al momento del click sul bottone.
Per maggiore chiarezza e semplicità di lettura del codice, ho scelto di utilizzare il meccanismo degli **alias**, definendo 
il tipo EventHandler all’interno dell’object _UIHelper_:

    private type EventHandler = ActionListener

Segue un esempio di creazione di un bottone utilizzando il metodo dell'helper e il costrutto **for yield** citato anche 
precedentemente:  

    val buttonsData     = Seq(
      (
        "Age Test",
        () =>
          frame.dispose()
          AgeTest(GamePanelsFactoryImpl(), ResultPanelsFactoryImpl()).show()
      ),
      (
        "Training",
        () =>
          frame.dispose()
          BrainTraining(ResultPanelsFactoryImpl()).show(GamePanelsFactoryImpl())
      ),
      ("Game Rules", () => showGameRulesDialog())
    )
    val components      = for ((btnData, idx) <- buttonsData.zipWithIndex) yield
      val button =
        UIHelper.createStyledButton(
          btnData._1,
          buttonSize,
          PIXEL_FONT25,
          handler = _ => { btnData._2() }
        )
      val strut  =
        if (idx < buttonsData.size - 1) Box.createVerticalStrut(BUTTON_DISTANCE)
        else Box.createVerticalStrut(LAST_BUTTON_DISTANCE)
      Seq(button, strut)


[Torna indietro](../Implementazione.md)