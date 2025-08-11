# Processo di sviluppo adottato

Per lo sviluppo del progetto, il gruppo ha deciso di adottare una strategia *agile*. Nello specifico,
anche come riportato nel paragrafo *P8* delle [regole di esame](https://virtuale.unibo.it/mod/page/view.php?id=1880168),
è stata scelta una metodologia *SCRUM-inspired*, dove i componenti del gruppo, oltre a essere tre sviluppatori, hanno
anche assunto i ruoli di committente e product owner. Per lo sviluppo del progetto, il gruppo ha utilizzato i seguenti 
strumenti:
* Git
* GitHub
* IntelliJ

## Modalità di divisione in itinere dei task

I task sono stati suddivisi in base alle priorità assegnate a ognuno di essi. Infatti, i task aventi una priorità
maggiore sono stati assegnati durante i meeting con tutti i componenti del gruppo mentre, i task con priorità inferiore
sono stati svolti dal membro il quale avesse già completato i task prioritari assegnati. Per essere considerato
completato, un task deve soddisfare i seguenti requisiti:
* il membro deve aver verificato la correttezza della soluzione tramite test manuali e/o automatici
* il membro deve aver integrato la soluzione prodotta con quella creata da gli altri membri

## Meeting/interazioni pianificate

A inizio progetto, il gruppo ha partecipato a un meeting iniziale dove venivano presentati l'analisi del dominio
e una possibile modellazione dell'architettura. In più, sono stati pianificati anche il numero e la durata degli sprint.
Per quanto riguarda quest'ultimi, il team ha deciso di organizzarsi in sprint settimanali con l'obiettivo di avere
una giusta quantità di tempo per realizzare i goal prefissati dallo sprint. In ogni sprint, l'ultimo giorno della
settimana veniva utilizzato per organizzare il meeting di fine sprint nel quale venivano discussi due principali argomenti:
1. quali obiettivi, tra quelli prefissati dallo sprint, sono stati portati a termine?
2. quali sono i nuovi obiettivi del prossimo sprint?

Avere meeting corposi ha permesso al gruppo di avere aggiornamenti continui sullo sviluppo del progetto, aspetto
di fondamentale importanza.

## Modalità di revisione in itinere dei task

La revisione di ogni task è stata svolta grazie a uno strumento di GitHub: la *pull request*. Infatti, avendo seguito
la classica struttura di *Git flow*, il processo è stato il seguente:
* lo sviluppatore incaricato del task apriva un nuovo branch da *develop* chiamandolo *feature/nomeFeature*
* in quel branch, lo sviluppatore faceva i propri commit per implementare il task
* una volta pronto, lo sviluppatore faceva una *pull request* per fare il merge con il branch *develop*
* per ottenere il merge, la *pull request* doveva essere accettata da almeno un membro del team

Questo metodo di lavoro ha permesso al team di rimanere sempre aggiornato sulle modifiche al progetto indotte da nuove
implementazioni.

## Scelta degli strumenti di test/build/continuous integration

Il testing automatico del progetto è stato eseguito utilizzando come strumento *Scalatest* essendo una tecnologia
semplice e facile da integrare. Per la fase di building, il gruppo ha preferito utilizzare *sbt* e non *Gradle* in quanto
era più facile da utilizzare con il linguaggio Scala. Infine, la *continuous integration* della relazione è garantita
dall'uso di *GitHub Pages* il quale produce un nuovo deploy a ogni modifica di essa.

[Torna all'indice](index.md)