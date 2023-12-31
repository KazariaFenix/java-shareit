package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "item_requests")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 256)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor", referencedColumnName = "id", nullable = false)
    private User requestor;
    @Column(nullable = false)
    private LocalDateTime created;
}
