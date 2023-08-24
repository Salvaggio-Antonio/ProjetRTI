/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author Salva
 */
public class Facture {
    private String reservation;
    private double prix;
    private double accompte;

    public String getReservation() {
        return reservation;
    }

    public Facture(String reservation, double prix, double accompte, double resteapayer) {
        this.reservation = reservation;
        this.prix = prix;
        this.accompte = accompte;
        this.resteapayer = resteapayer;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getAccompte() {
        return accompte;
    }

    public void setAccompte(double accompte) {
        this.accompte = accompte;
    }

    public double getResteapayer() {
        return resteapayer;
    }

    public void setResteapayer(double resteapayer) {
        this.resteapayer = resteapayer;
    }
    private double resteapayer;
    
}
