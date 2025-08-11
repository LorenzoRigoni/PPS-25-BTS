# Testing

Come già specificato nelle precedenti sezioni, il progetto è stato sviluppato con un approccio di *Testing Driven Development*. Vista la natura del progetto, si è cercato di aderire in maniera quanto più rigorosa possibile a tale approccio, scrivendo i test unitari (rispetto alle singole funzioni) prima di scrivere il codice che implementa le funzionalità richieste.

## Tecnologie usate

Per la scrittura e l'esecuzione dei test è stato utilizzato **ScalaTest**, uno dei framework di testing più diffusi nell’ecosistema Scala. Questo ha permesso di definire in maniera semplice e leggibile suite di test per ciascun modulo logico sviluppato.

## Metodologia usata

Ogni funzionalità è stata implementata seguendo l’approccio TDD:

- Scrittura dei test unitari prima dello sviluppo della logica.
- Esecuzione dei test per verificarne il fallimento iniziale.
- Implementazione della logica fino al superamento dei test.
- Refactoring del codice mantenendo i test verdi.

Le suite di test coprono tutte le principali funzionalità:

- **FastCalc**: verifica della correttezza delle risposte generate e della validazione di risposte corrette e scorrette.
- **RightDirections**: test sulle operazioni logiche (Not, And, Or) e sulla corretta valutazione degli alberi.
- **CountWords**: verifica del numero di parole in frasi generate e della corretta validazione delle risposte.
- **ColoredCount**: verifica del numero di parole in frasi generate e della corretta validazione delle risposte.
- **Word Memory**: verifica del grado di correttezza della risposta e della generazione delle domande.

## Grado di copertura

Tutte le principali funzionalità del progetto sono coperte da test unitari. Ogni test è stato progettato per garantire:

- la correttezza della logica interna,
- il rispetto delle specifiche per ogni mini-gioco,
- la robustezza rispetto a input non validi o situazioni limite.

I test sono stati eseguiti regolarmente durante lo sviluppo per assicurare stabilità e coerenza del codice.

[Torna all'indice](index.md)
