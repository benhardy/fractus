package net.aethersanctum.fractus.util

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton

/**
 * make dealing with Swing less painful
 */
object SwingUtil {

  /**convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: => Any) = new ActionListener() {
    override def actionPerformed(e: ActionEvent) {
      f
    }
  }

  /**convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: ActionEvent => Any) = new ActionListener() {
    override def actionPerformed(e: ActionEvent) {
      f(e)
    }
  }

  /**create a simple button with an attached Action block */
  def button(label: String)(todo: => Any) = {
    val gadget = new JButton(label)
    gadget.addActionListener(todo)
    gadget
  }
}