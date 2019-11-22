package gcv.beans;

/**Enumération contenant les différentes natures possibles
 * d'une activité d'un CV.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.beans.Activity
 */
public enum Nature {
    /** Lorsque l'activité est une expérience professionnelle.
     */
    WORK_EXP,
    /** Lorsque l'activité est une formation.
     */
    EDUCATION,
    /** Lorsque l'activité est un projet.
     */
    PROJECT,
    /** Lorsque l'activité n'a pas pour nature celles énoncées au dessus.
     */
    OTHER
}