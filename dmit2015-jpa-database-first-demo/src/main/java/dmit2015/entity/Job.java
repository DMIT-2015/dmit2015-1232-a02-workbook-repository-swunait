package dmit2015.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "JOBS", schema = "HR")
public class Job {

    @Id
    @Column(name = "JOB_ID", nullable = false, length = 10)
    private String jobId;
    @Basic
    @Column(name = "JOB_TITLE", nullable = false, length = 35)
    private String jobTitle;
    @Basic
    @Column(name = "MIN_SALARY", nullable = true, precision = 0)
    private Integer minSalary;
    @Basic
    @Column(name = "MAX_SALARY", nullable = true, precision = 0)
    private Integer maxSalary;
    @OneToMany(mappedBy = "jobsByJobId")
    private Collection<Employee> employeesByJobId;
    @OneToMany(mappedBy = "jobsByJobId")
    private Collection<JobHistory> jobHistoriesByJobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(jobId, job.jobId) && Objects.equals(jobTitle, job.jobTitle) && Objects.equals(minSalary, job.minSalary) && Objects.equals(maxSalary, job.maxSalary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, jobTitle, minSalary, maxSalary);
    }

    public Collection<Employee> getEmployeesByJobId() {
        return employeesByJobId;
    }

    public void setEmployeesByJobId(Collection<Employee> employeesByJobId) {
        this.employeesByJobId = employeesByJobId;
    }

    public Collection<JobHistory> getJobHistoriesByJobId() {
        return jobHistoriesByJobId;
    }

    public void setJobHistoriesByJobId(Collection<JobHistory> jobHistoriesByJobId) {
        this.jobHistoriesByJobId = jobHistoriesByJobId;
    }
}
