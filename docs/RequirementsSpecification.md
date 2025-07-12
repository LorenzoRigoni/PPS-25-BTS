# Requirements specification 
Questa sezione della documentazione contiene una descrizione dei requisiti del sistema, suddivisi in: requisiti di business, 
analisi dei requisiti e modello di dominio, requisiti funzionali (utente e di sistema), requisiti non funzionali, requisiti 
di implementazione e requisiti opzionali.

## Requisiti di business 
* Creare un sistema che permetta di giocare ad una versione semplificata di Brain Training.
* Creare la modalità di gioco “Age Test” che permetta di stimare l’età cerebrale dell’utente in seguito al completamento di 3 minigiochi.
* Creare la modalità “Training” che permetta all’utente di allenare le proprie capacità cognitive focalizzandosi sulle abilità su cui ritiene di avere maggiori carenze/difficoltà.


## Modello di dominio
Il progetto prevede lo sviluppo di una versione personalizzata del gioco Brain Training, focalizzata su due principali modalità di gioco:
1. **Age test**: In questa modalità, l’utente affronta automaticamente tre minigiochi in sequenza, selezionati tra sfide di velocità, memoria, calcolo e altre abilità cognitive.  Al termine della sessione, il sistema calcola e mostra una stima dell’età cerebrale del giocatore. Questo valore è ottenuto analizzando il numero di errori commessi e il tempo impiegato per completare ciascun minigioco.
2. **Training**: Questa modalità ha lo scopo di offrire all’utente un’esperienza di allenamento personalizzata. L’utente può scegliere liberamente quale minigioco affrontare, concentrandosi sulle aree in cui ritiene di avere più difficoltà. L’obiettivo è incentivare il miglioramento progressivo delle proprie capacità cognitive, attraverso sessioni mirate e ripetibili.

Nel progetto verranno implementati i seguenti minigiochi, pensati per stimolare le diverse abilità cognitive:
* **Fast Calc**: Viene mostrata una sequenza di operazioni aritmetiche su una singola riga. L’utente deve calcolare mentalmente il risultato e inserirlo nel minor tempo possibile, cercando di evitare errori.
* **Count Words**: All’utente viene presentata una frase composta da più parole. Il compito consiste nel contare il numero esatto di parole e inserirlo come risposta, sempre nel minor tempo possibile.
* **Right Directions**: Viene mostrata una sequenza di indicazioni di direzione, a cui l’utente dovrà reagire inserendo input da tastiera. L’utente deve simulare uno spostamento seguendo le istruzioni tramite input da tastiera. Il gioco è completato con successo se la posizione finale dell’utente corrisponde a quella attesa dal sistema.

Ciascun gioco, mano a mano che scorre il tempo, proporrà quesiti di difficoltà maggiore.
I giochi sopra elencati stimolano varie abilità cognitive, come il tempo di reazione, la memoria, l’attenzione, la comprensione del linguaggio e la rapidità di calcolo.

## Requisiti funzionali 
In questa sezione verranno analizzati i requisiti funzionali del progetto, evidenziando le funzionalità messe a disposizione suddividendole per utente e sistema.

### Utente
L’utente deve aver la possibilità di:
* Avviare una nuova partita in entrambe le due modalità di gioco previste.
* Scegliere un minigioco specifico su cui allenarsi.
* Visualizzare le istruzioni di ciascun minigioco.
* Inserire input da mouse e da tastiera per completare i minigiochi.
* Visualizzare i risultati conseguiti sia alla fine dell’ age test, ma anche al termine dell’allenamento. Nel primo caso si tratterà di una stima dell’età cerebrale, nel secondo caso verrà mostrato il tempo impiegato e il numero di errori totalizzati.
* Durante ciascun minigioco l’utente potrà visualizzare il tempo residuo al termine della partita.

### Di sistema 
Il sistema deve essere in grado di:
* Permettere la selezione di una delle due modalità di gioco previste.
* Permettere la selezione di un minigioco nella modalità Training.
* Avviare automaticamente tre minigiochi casuali nella modalità Age Test.
* Mostrare le istruzioni relative al gioco selezionato.
* Misurare il tempo impiegato e il numero di risposte esatte ed errate.
* Verificare la correttezza della risposta inserita dall’utente.
* Calcolare la stima dell’età cerebrale al termine dei tre minigiochi.
* Mostrare i risultati conseguiti dal giocatore in entrambe le modalità di gioco.
* Gestire la logica dei singoli minigiochi.
* Gestire errori di input
* Aumentare la difficoltà dei quesiti allo scorrere del tempo.
* Generare i quesiti in modo random in base al minigioco in corso (es. espressioni aritmetiche nel caso del gioco _Fast Calc_).

## Requisiti non funzionali
Il sistema deve inoltre possedere le seguenti qualità:
* **Prestazioni**: Il sistema deve rispondere agli input dell’utente in breve tempo e il tempo di caricamento tra un gioco e l’altro non deve superare i 3 secondi. Anche il calcolo dell’età cerebrale deve essere rapido, l’utente non deve attendere i risultati.
* **Scalabilità**: L’architettura deve consentire l'aggiunta di nuovi minigiochi senza la necessità di modificare eccessivamente il codice esistente.
* **Affidabilità**: Il sistema deve gestire correttamente input errati.
* **Usabilità**: L’interfaccia utente dovrà essere facilmente comprensibile e intuitiva, per utenti di qualsiasi fascia d’età. Devono essere sempre mostrate le istruzioni di ciascun minigioco, spiegandone il funzionamento in modo chiaro e sintetico.

## Requisiti di implementazione
Questa sezione descrive i vincoli tecnici e le tecnologie utilizzate per lo sviluppo del progetto:
* Linguaggio di programmazione: _Scala_ 3.3.6
* Interfaccia grafica: _Java Swing_
* Analisi statica della qualità del codice: _Scalaftm, Wartremover e Scalafix_
* Analisi dinamica (Testing e copertura del codice:) _Scalatest e Scoverage_ 

## Requisiti opzionali 
Il gruppo ha individuato i seguenti requisiti opzionali, non essenziali per il funzionamento di base, ma potenzialmente utili per arricchire il sistema:
* **Modalità Multiplayer (sfida 1 vs 1)**:  Il sistema potrebbe offrire, in futuro, una modalità multiplayer che consenta a due utenti di sfidarsi nello stesso minigioco, giocando in parallelo e confrontando i risultati (tempi, punteggio, errori). Sebbene questa funzionalità non sia prevista nella versione originale di Brain Training e non influenzi direttamente l’obiettivo principale del gioco, essa può aumentare il coinvolgimento dell’utente.
* **Statistiche**: Il sistema potrebbe conservare uno storico dei risultati ottenuti dall’utente per ciascun minigioco. Queste informazioni permetterebbero all’utente di monitorare l’andamento delle proprie abilità cognitive nel tempo, identificando eventuali miglioramenti o regressi

[Torna all'indice](index.md)