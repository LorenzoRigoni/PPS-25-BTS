# Requirements specification 
Questa sezione della documentazione contiene una descrizione dei requisiti del sistema, suddivisi in: requisiti di business, 
analisi dei requisiti e modello di dominio, requisiti funzionali (utente e di sistema), requisiti non funzionali, requisiti 
di implementazione e requisiti opzionali.

## Requisiti di business 
* Creare un sistema che permetta di giocare a una versione semplificata di Brain Training.
* Creare la modalità di gioco “Age Test” che permetta di stimare l’età cerebrale dell’utente in seguito al completamento 
di 3 mini giochi.
* Creare la modalità “Training” che permetta all’utente di giocare a un mini gioco a sua scelta e allenare le proprie 
capacità cognitive focalizzandosi sulle abilità su cui ritiene di avere maggiori carenze/difficoltà.

## Modello di dominio
Il progetto prevede lo sviluppo di una versione personalizzata del gioco Brain Training, focalizzata su due principali 
modalità di gioco:
1. **Age test**: In questa modalità, l’utente affronta automaticamente tre mini giochi in sequenza, selezionati tra sfide
di velocità, memoria, calcolo e altre abilità cognitive.  Al termine della sessione, il sistema calcola e mostra una stima 
dell’età cerebrale del giocatore. Questo valore è ottenuto analizzando il numero di errori commessi e il tempo impiegato 
per completare ciascun mini gioco.
2. **Training**: Questa modalità ha lo scopo di offrire all’utente un’esperienza di allenamento personalizzata. L’utente
può scegliere liberamente quale mini gioco affrontare, concentrandosi sulle aree in cui ritiene di avere più difficoltà. 
L’obiettivo è incentivare il miglioramento progressivo delle proprie capacità cognitive, attraverso sessioni mirate 
e ripetibili.

Il progetto prevede i seguenti mini giochi, pensati per stimolare le diverse abilità cognitive:
* **Fast Calc**: Viene mostrata un'espressione composta da numeri a una cifra e operatori (somma, sottrazione e moltiplicazione).
L’utente deve calcolare mentalmente il risultato e inserirlo nel minor tempo possibile, cercando di evitare errori.
* **Count Words**: All’utente viene presentata una frase composta da più parole. Il compito dell'utente consiste nel contare il numero 
esatto di parole e inserirlo come risposta, sempre nel minor tempo possibile.
* **Right Directions**: Viene mostrata una sequenza di indicazioni di direzioni combinata con operatori logici (and, or, not),
a cui l’utente dovrà reagire inserendo la risposta da tastiera tramite e tasti W, A, S, e D. La risposta viene considerata 
corretta se rientra in nell'insieme di possibili 
risposte corrette per quella domanda.
* **Colored Count**: Viene mostrata una sequenza di numeri di colore diverso e l'utente dovrà indicare quanti sono i 
numeri mostrati che hanno lo stesso colore che viene richiesto dalla domanda. 
* **Word Memory**: Viene mostrata una sequenza di parole estratte in modo casuale che rimangono visibili all'utente per 
10 secondi in modo che abbia tempo per memorizzarle. In questo breve intervallo di tempo il campo per l'inserimento della
risposta sarà disabilitato. Allo scadere dei dieci secondi, l'utente dovrà digitare tutte le parole che ricorda, prestando 
attenzione a scriverle correttamente. Verranno considerate risposte corrette, tutte quelle che contengono almeno il 60% 
delle parole mostrate inizialmente (indipendentemente dall'ordine con cui vengono inserite). 

Per ciascun gioco è possibile indicare il numero di round che dovranno essere giocati e quindi il numero di quesiti da 
generare prima che il gioco possa essere considerato terminato.
All'aumentare dei round deve aumentare anche la difficoltà dei quesiti. 
I giochi sopra elencati stimolano varie abilità cognitive, come il tempo di reazione, la memoria, l’attenzione, la comprensione 
del linguaggio e la rapidità di calcolo.

## Requisiti funzionali 
In questa sezione verranno analizzati i requisiti funzionali del progetto, evidenziando le funzionalità messe a disposizione suddividendole per utente e sistema.

### Utente
L’utente deve aver la possibilità di:
* Avviare una nuova partita in entrambe le due modalità di gioco previste.
* Scegliere un mini gioco specifico su cui allenarsi se gioca in modalità Training.
* Visualizzare le regole di ciascun mini gioco.
* Inserire input da tastiera per completare i mini giochi.
* Visualizzare i risultati conseguiti nell'Age Test, ossia una stima dell'età celebrale. 
* Visualizzare i risultati ottenuti nel mini gioco scelto (Training), ossia il numero di risposte corrette, il numero di
risposte errate e il tempo impiegato per completare il gioco.
* Durante ciascun mini gioco l’utente potrà notare un incremento della difficoltà dei quesiti mano a mano che il gioco avanza. 

### Di sistema 
Il sistema deve essere in grado di:
* Permettere la selezione di una delle due modalità di gioco previste.
* Permettere la selezione di un minigioco nella modalità Training.
* Permettere di visualizzare le regole delle due modalità di gioco e dei singoli mini giochi. 
* Avviare automaticamente tre minigiochi casuali nella modalità Age Test.
* Misurare il tempo impiegato e il numero di risposte esatte ed errate relativamente al gioco selezionato nella modalità Training.
* Verificare la correttezza della risposta inserita dall’utente.
* Calcolare la stima dell’età cerebrale al termine dei tre minigiochi in modalità Age Test. 
* Mostrare i risultati conseguiti dal giocatore in entrambe le modalità di gioco.
* Gestire la logica dei singoli mini giochi.
* Gestire errori di input (es. risposte vuote).
* Aumentare la difficoltà dei quesiti durante l'avanzamento del gioco.
* Costruire i quesiti in modo random in base al mini gioco in corso (es. espressioni aritmetiche nel caso del gioco _Fast Calc_).

## Requisiti non funzionali
Il sistema deve inoltre possedere le seguenti qualità:
* **Prestazioni**: Il sistema deve rispondere agli input dell’utente in breve tempo e il tempo di caricamento tra un 
gioco e l’altro non deve superare i 3 secondi. Anche il calcolo dell’età cerebrale deve essere rapido, l’utente non deve 
attendere i risultati.
* **Scalabilità**: L’architettura deve consentire l'aggiunta di nuovi mini giochi senza la necessità di modificare eccessivamente 
il codice esistente.
* **Affidabilità**: Il sistema deve gestire correttamente input errati.
* **Usabilità**: L’interfaccia utente dovrà essere facilmente comprensibile e intuitiva, per utenti di qualsiasi fascia d’età. 
Devono essere sempre mostrate le istruzioni di ciascun mini gioco, spiegandone il funzionamento in modo chiaro e sintetico.

## Requisiti di implementazione
Questa sezione descrive i vincoli tecnici e le tecnologie utilizzate per lo sviluppo del progetto:
* Linguaggio di programmazione: _Scala_ 3.3.6
* Interfaccia grafica: _Java Swing_
* Analisi statica della qualità del codice: _Scalaftm, Wartremover e Scalafix_
* Analisi dinamica (Testing e copertura del codice:) _Scalatest e Scoverage_ 

## Requisiti opzionali 
Il gruppo ha individuato i seguenti requisiti opzionali, non essenziali per il funzionamento di base, ma potenzialmente 
utili per arricchire il sistema:
* **Modalità Multiplayer (sfida 1 vs 1)**:  Il sistema potrebbe offrire, in futuro, una modalità multiplayer che consenta 
a due utenti di sfidarsi nello stesso mini gioco, giocando in parallelo e confrontando i risultati (tempi, punteggio, errori). 
Sebbene questa funzionalità non sia prevista nella versione originale di Brain Training e non influenzi direttamente l’obiettivo 
principale del gioco, essa può aumentare il coinvolgimento dell’utente.
* **Statistiche**: Il sistema potrebbe conservare uno storico dei risultati ottenuti dall’utente per ciascun mini gioco. 
Queste informazioni permetterebbero all’utente di monitorare l’andamento delle proprie abilità cognitive nel tempo, 
identificando eventuali miglioramenti o regressi

[Torna all'indice](index.md)