package com.pilaf.warhammerstats;


import java.util.List;

public class Unit {
    private String name;
    private Race race;
    private Size size;
    private int unitAmount;
    private int hitPointsPerUnit;

    private int attack;
    private int defence;
    private int armor;
    private int damage;
    private int speed;
    private double meleeInterval;
    private int apDamage;
    private int leadership;
    private int chargeBonus;
    private int physicalResistance;
    private int magicalResistance;
    private int fireResistance;
    private int maxUnitAttackAmount;
    private int bonusAgainstLarge;
    private int bonusAgainstInfantry;
    private List<Skills> skillsList;

    public double calculateAttackChance(Unit target){
        double attackChange = 0.7 + ((this.attack
                - target.defence
                + calculateBonus(target.size))/100
        );
        if (attackChange > 1){
            attackChange =  1;
        } else if (attackChange < 0.05){
            attackChange = 0.05;
        }
        return attackChange;
    }


    public double calculateAverageDamage(Unit target){
        double apDamageComputed = this.apDamage;
        if (isMagicalOrFireAttack()){
            apDamageComputed = (this.apDamage + calculateBonusDamage(target.size, this.apDamage))
                    * calculateResistanceModifier(target.magicalResistance)
                    * calculateResistanceModifier(target.fireResistance);
        } else {
            apDamageComputed = this.apDamage * calculateResistanceModifier(target.physicalResistance);
        }
        double standardDamageComputed = this.damage;
        if (isMagicalOrFireAttack()){
            standardDamageComputed = (this.damage + calculateBonusDamage(target.size, this.damage))
                    * calculateResistanceModifier(target.magicalResistance)
                    * calculateResistanceModifier(target.fireResistance)
                    * calculateArmorReduction(target.armor);
        } else {
            standardDamageComputed = (this.damage + calculateBonusDamage(target.size, this.damage))
                    * calculateResistanceModifier(target.physicalResistance)
                    * calculateArmorReduction(target.armor);
        }
        return apDamageComputed + standardDamageComputed;
    }


    private int calculateBonus(Size size) {
        int bonus = 0;
        if (size.equals(Size.INFANTRY)){
            bonus += this.bonusAgainstInfantry;
        } if (size.equals(Size.LARGE)){
            bonus += this.bonusAgainstLarge;
        }
        return bonus;
    }

    private double calculateBonusDamage(Size size, int damage){
        double bonus = 0;
        if (size.equals(Size.INFANTRY)){
            bonus += (this.bonusAgainstInfantry * (damage / (this.damage + this.apDamage)));
        } if (size.equals(Size.LARGE)){
            bonus += (this.bonusAgainstLarge * (damage / (this.damage + this.apDamage)));
        }
        return bonus;
    }


    private double calculateArmorReduction(int armor) {
        return (armor + (armor / 2)) / 2;
    }

    private boolean isMagicalOrFireAttack() {
       return this.skillsList.contains(Skills.MAGICAL_ATTACK) || this.skillsList.contains(Skills.FIRE_ATTACK);
    }

    private  double calculateResistanceModifier(int resistance){
        return ((100 - resistance) / 100);

    }


}
