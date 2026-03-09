package io.moji.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

  @Override
  public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> observer) {
    log.info("createBillingAccount request received {}", request.toString());

    // Business logic
    BillingResponse response = BillingResponse.newBuilder()
        .setAccountId("12345")
        .setStatus("ACTIVE")
        .build();

    observer.onNext(response);
    observer.onCompleted();
  }
}
