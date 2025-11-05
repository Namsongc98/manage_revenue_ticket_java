package com.example.manage_revenue_ticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Salary extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    // 🔗 Mỗi phiếu lương thuộc về 1 nhân viên (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "salaries_fk_user"))
    @JsonIgnore
    private User user;

    @Column(name = "salary_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal salary;

    @Column(name = "period_month", nullable = false)
    private Byte periodMonth;

    @Column(name = "period_year", nullable = false)
    private Short periodYear;

}
