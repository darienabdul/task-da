package edu.gatech.seclass.jobcompare6300.models;

public class Job {
    public String title;
    public String company;
    public Location location;
    public long salary;
    public long bonus;
    public int match401K;
    public int internetStipend;
    public int accidentInsurance;
    public int tuitionReimbursement;

    public boolean isCurrentJob;

    public Job(){}

    public Job(String title, String company, Location location, long salary, long bonus, int match401K, int internetStipend, int accidentInsurance, int tuitionReimbursement){
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.bonus = bonus;
        this.match401K = match401K;
        this.internetStipend = internetStipend;
        this.accidentInsurance = accidentInsurance;
        this.tuitionReimbursement = tuitionReimbursement;
    }

    public double getAdjustedSalary() {
        return salary / (location.costOfLivingIndex / 100.0);
    }

    public double getAdjustedBonus() {
        return bonus / (location.costOfLivingIndex / 100.0);
    }

    @Override
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()) return false;
        if(this == o) return true;
        Job job = (Job) o;
        return title.equals(job.title)
                && company.equals(job.company)
                && location.equals(job.location)
                && salary== job.salary
                && bonus==job.bonus
                && match401K==job.match401K
                && internetStipend==job.internetStipend
                && accidentInsurance==job.accidentInsurance
                && tuitionReimbursement== job.tuitionReimbursement;
    }
}