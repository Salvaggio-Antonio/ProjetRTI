/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveurs;

/**
 *
 * @author Salva
 */
public interface SourceTaches
// synchronized ne s'utilise pas dans un interface
{
    public Runnable getTache() throws InterruptedException;
    public boolean existTaches();
    public void recordTache (Runnable r);
}
