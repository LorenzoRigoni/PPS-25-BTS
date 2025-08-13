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
aderiscono al contratto *MiniGameLogic* e poi, ognuno di loro, implementerà i suoi metodi in base alle regole
del mini-gioco. Il riferimento al trait *MiniGameLogic* non si trova direttamente nel controller ma lo si può trovare
nella classe *MiniGameAdapter*. Questa scelta è stata presa in quanto ogni mini-gioco può presentare diversi modi di
implementare i tipi di domande, le risposte accettate e il tipo di validazione fornita. La classe *MiniGameAdapter*
implementa il trait *MiniGameWrapper* il quale contiene le funzioni base del mini-gioco.


### View

### Controller

## Organizzazione del codice

Il codice è stato diviso in *package* in base alle funzionalità. Nello specifico, il progetto presenta quattro moduli:

- **utils**: contiene constanti, funzioni ed *enum* utili per due o più file diversi
- **models**: contiene tutte le logiche del progetto, sia quelle dei mini-giochi e sia quelle del calcolo dei risultati
- **views**: contiene tutte le grafiche del progetto, dai menu di gioco fino ai pannelli dei mini-giochi
- **controllers**: contiene il controller del gioco e le classi/funzioni usate da esso

[Torna all'indice](index.md)