# Coverage-Graphs 
## Knowledge acquisition with forgetting: an incremental and developmental rule-based setting [THESIS]

###APPROACH

In this work we take a most general approach by considering that we start with an off-the-shelf inductive engine (e.g., a rule learning algorithm, an inductive logic programming (ILP) system or an inductive programming (IP) system) and an off-the-shelf deductive engine (e.g., a coverage checker, an automated deduction system or a declarative programming language) and, over them, we construct a long-life knowledge discovery system. For this purpose, several issues have to be addressed:

1. The inductive engine can generate many possible hypotheses and patterns. Once brought to working memory we require metrics to evaluate how these hypotheses behave and how they are related in the context of previous knowledge.
2. As working memory and computational time are limited, we need an forgetting criterion to discard some rules.
3. The deductive engine checks the coverage of each hypothesis independently, using the background or consolidated knowledge as auxiliary rules, but not other working rules.
4. The promotion of rules into consolidated knowledge must avoid unnecessarily large knowledge bases and the consolidation of rules that are useless, too preliminary or inconsistent.

The idea of coverage graph is used as the basis for structuring knowlege and is delegated to the deductive engine. The generation of new rules is delegated to the inductive engine. The crucial part is the definition of appropriate metrics to guide the way knowledge develops. For this purpose, the MML principle is used as a sound theoretical ground for the metrics.

###NOTES

Code, libraries and examples related to the articles: 

- **Forgetting and consolidation for incremental and cumulative knowledge acquisition systems**
  Fernando Martínez-Plumed, Cèsar Ferri, José Hernández-Orallo, M. José Ramírez-Quintana 
  arXiv:1502.05615 (2015)
  
- **A knowledge growth and consolidation framework for lifelong machine learning systems**
  Fernando Martínez-Plumed, Cèsar Ferri, José Hernández-Orallo, M. José Ramírez-Quintana
  Proceedings of 13rd International Conference on Machine Learning and Applications, ICMLA 2014 ,
  Detroit, USA on December 3-6, 2014 IEEE 

###FILES INCLUDED

- JGraphXAdapterDemo.java 
- JGraphXAdapterDemo2.java
- MyGraphUtil.java

###HOW TO

Technology:

    Java
    jgrapht libraries

Compile and Run JGraphXAdapterDemo.java 


