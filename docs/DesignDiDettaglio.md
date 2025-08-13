# Design di dettaglio

## Scelte rilevanti di design

Come detto precedentemente, la scelta rilevante del progetto è relativa all'uso del pattern *MVC*. Questa decisione
ci ha permesso di tenere separata la logica dalla grafica, rendendo così il codice facilmente modificabile
ed estendibile nel futuro. Inoltre, il progetto prevede un'architettura modulare dove ogni funzionalità del sistema
è racchiusa in un modulo. Infine, il *controller* rappresenta il punto centrale del progetto in quanto incapsula
lo stato attuale del gioco, andando così a garantire coerenza tra le diverse viste.

## Pattern di progettazione

### Model

Per lo sviluppo delle logiche dei mini-giochi è stato usato il pattern *Strategy*. Infatti, tutti i mini-giochi
aderiscono al contratto *MiniGameLogic* e poi, ognuno di loro, implementerà i propri metodi in base alle regole
del mini-gioco. Le funzioni fondamentali di ogni mini-gioco sono:
- generare nuove domande
- fare il *parsing* delle risposte dell'utente
- controllare le risposte dell'utente
- controllare quando il mini-gioco è finito

![ModelDesign](img/model_design.png)

### View

### Controller

Il controller si occupa di gestire l'andamento del gioco facendo collaborare la logica e la grafica. La comunicazione
con la logica viene fatta attraverso la collezione di logiche del tipo *MiniGameLogic* (implementato tramite il
pattern *Strategy* visto prima). La comunicazione con la grafica, invece, viene fatta tramite il trait 
*GameViewCallback*. Questo contratto viene implementato dalle *view* in modo che, quando il controller produce un
nuovo evento potrà richiamare direttamente i metodi del trait. I due eventi previsti sono:
- **onGameChanged** che si verifica quando un mini-game è finito e si passa al successivo
- **onGameFinished** che si verifica quando il gioco (che sia Age Test o Brain Training) è finito

Infine, il controller tiene traccia delle risposte dell'utente salvandole in una collezione di *QuestionResult*.

![ControllerDesign](img/controller_design.png)


## Organizzazione del codice

Il codice è stato diviso in *package* in base alle funzionalità. Nello specifico, il progetto presenta quattro moduli:

- **utils**: contiene costanti, funzioni ed *enum* utili per due o più file diversi
- **models**: contiene tutte le logiche del progetto, sia quelle dei mini-giochi e sia quelle del calcolo dei risultati
- **views**: contiene tutte le grafiche del progetto, dai menu di gioco fino ai pannelli dei mini-giochi
- **controllers**: contiene il controller del gioco e le classi/funzioni usate da esso

[Torna all'indice](index.md)