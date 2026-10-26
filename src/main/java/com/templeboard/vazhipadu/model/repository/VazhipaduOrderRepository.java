package com.templeboard.vazhipadu.model.repository;

import com.templeboard.vazhipadu.model.entity.VazhipaduOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VazhipaduOrderRepository extends JpaRepository<VazhipaduOrder, Long>, JpaSpecificationExecutor<VazhipaduOrder> {
}
