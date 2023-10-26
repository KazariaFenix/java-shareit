package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime startTime;
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime endTime;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id", nullable = false)
    private User booker;
    @Enumerated(EnumType.STRING)
    private StatusBooking status;
}
