package com.example.manage_revenue_ticket.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    // 🔗 Mỗi phiếu lương thuộc về 1 nhân viên (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "salaries_fk_user"))
    private User user;

    @Column(name = "base_salary", precision = 12, scale = 2, nullable = false)
    private BigDecimal baseSalary;

    @Column(name = "allowance", precision = 12, scale = 2, nullable = false)
    private BigDecimal allowance = BigDecimal.ZERO;

    @Column(name = "commission", precision = 12, scale = 2, nullable = false)
    private BigDecimal commission = BigDecimal.ZERO;

    @Column(name = "bonus", precision = 12, scale = 2, nullable = false)
    private BigDecimal bonus = BigDecimal.ZERO;

    @Column(name = "period_month", nullable = false)
    private Byte periodMonth;

    @Column(name = "period_year", nullable = false)
    private Short periodYear;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
