package cz.scholz.sandbox.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class AdminApiClientAsyncTest {
    @Test
    @Timeout(value = 60)
    public void testFreecode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Admin admin = adminClient();

        admin.listTopics().names()
                .whenComplete((topics, error) -> {
                    if (error != null) {
                        System.out.println("Failed: " + error);
                        Assertions.fail(error);
                    } else {
                        System.out.println("Succeeded: " + topics);

                        admin.describeTopics(topics).allTopicNames()
                                .whenComplete((topicNames, error2) -> {
                                    if (error2 != null) {
                                        System.out.println("Failed2: " + error2);
                                        Assertions.fail(error2);
                                    } else {
                                        System.out.println("Succeeded2: " + topicNames);
                                        latch.countDown();
                                    }
                                });
                    }
                });

        latch.await();
        admin.close();
    }

    Admin adminClient() {
        Properties props = new Properties();
        //props.put("security.protocol", "SSL");
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.73:32298");
        //props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PEM");
        //props.put(SslConfigs.SSL_KEYSTORE_CERTIFICATE_CHAIN_CONFIG, "-----BEGIN CERTIFICATE-----\n" +
        //        "MIIELTCCAhWgAwIBAgIUFjd569fZQbdgFFjFiPHUqMX0aWIwDQYJKoZIhvcNAQEN\n" +
        //        "BQAwLTETMBEGA1UECgwKaW8uc3RyaW16aTEWMBQGA1UEAwwNY2xpZW50cy1jYSB2\n" +
        //        "MDAeFw0yNDA3MTIyMjM4MDZaFw0yNzA0MDgyMjM4MDZaMBIxEDAOBgNVBAMMB215\n" +
        //        "LXVzZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDSP13FiIJ0WLK6\n" +
        //        "3lPsRWQUmdyUuHpMOorWBfHxWad7vlHa5VGBDL38YOfk3jv+VmTiaNr+QC1VBgbJ\n" +
        //        "3NZirejQBrjJEXVwPJufY5soYIC7w7VyN1+rHfu27RMjEEm0zxdSszR1AmLTUIBd\n" +
        //        "rbwBLuJrlgKYQXh98g8punG9Jw+QvJS43qeOFbTw17n2Z7OK1LYItV1duzfbGg6y\n" +
        //        "ON4ukV6AHT1IY5Km7HO2jgGhG9WuPkz5+y7UF4+k/6cRW+uHZdcHB83aH1p+hoTa\n" +
        //        "psKdIpaiNkcAeV6myUFPKUCSI+4oyuSDTcfKLhEwLWltxdw/IkaJ5ifEoN0BBOSR\n" +
        //        "bheoYA3tAgMBAAGjYDBeMB0GA1UdDgQWBBTA8ntoxWdmZuHsI7BCFsOtcKs0eDAM\n" +
        //        "BgNVHRMBAf8EAjAAMA4GA1UdDwEB/wQEAwIFoDAfBgNVHSMEGDAWgBQseUKr+Tgr\n" +
        //        "2MsSaDRj6ZXq8ivSWTANBgkqhkiG9w0BAQ0FAAOCAgEAg9U+3F6MW/0SawccQatl\n" +
        //        "1QEHXD/xJko+3o7lcHJP0LEOyWf28WS5nIse0tHXUSvW5vIo4R+WCtngPytv/i+z\n" +
        //        "xWrG3q+x207hJN++nGZ28QYdLjAktcdbExScW/RTPMVA428QUL0w9+hktQD/2J9I\n" +
        //        "CweYEd58hGDp8JmKp6GwEn4leRgtHdL2mcaYZCe0szy9G4pMyh6yNMZP6d3oaXKA\n" +
        //        "qrJCgTjHbumbw4sFdwiCMZj2/6Hn7syxcmR5JSw8uzOVMzEywyCmz46lorUMK7e/\n" +
        //        "eJYG+JDcwpuYRscOS8v0Q0U6vmgC1pLGzPMk9mVdIyjg5POTgOH5hWflFD3lscow\n" +
        //        "zpYVA05VkmS2YUyPojGIwDNDuaTvOiKgw8PxiCCkQjgAY0w41f3L3gKlGO13ygAN\n" +
        //        "1dgqb7B+62rdcYyYS6/O3eF51/7hSApIfzByKwZ+92byT6nwC0K4LunmczIP1qC8\n" +
        //        "QDMSKliGE9rNc10xCcJY4iVu9Tfj6V1/e4x39KTNH146nL8z70R/iIz4SSShKJdm\n" +
        //        "JC+RaM8rM0ua2kKr2WK0QebfScNADHs0CKHU3jALjPK/Wxcd0RIr+P1EkZhrBozV\n" +
        //        "zOWTSbctjl2kJp7pIwgmB65NHUXDImFMxXMZfdmcSPNshRYSyFOcRSkyfzESW27j\n" +
        //        "SEl2i5QvIHxWor54h//s8fI=\n" +
        //        "-----END CERTIFICATE-----");
        //props.put(SslConfigs.SSL_KEYSTORE_KEY_CONFIG, "-----BEGIN PRIVATE KEY-----\n" +
        //        "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDSP13FiIJ0WLK6\n" +
        //        "3lPsRWQUmdyUuHpMOorWBfHxWad7vlHa5VGBDL38YOfk3jv+VmTiaNr+QC1VBgbJ\n" +
        //        "3NZirejQBrjJEXVwPJufY5soYIC7w7VyN1+rHfu27RMjEEm0zxdSszR1AmLTUIBd\n" +
        //        "rbwBLuJrlgKYQXh98g8punG9Jw+QvJS43qeOFbTw17n2Z7OK1LYItV1duzfbGg6y\n" +
        //        "ON4ukV6AHT1IY5Km7HO2jgGhG9WuPkz5+y7UF4+k/6cRW+uHZdcHB83aH1p+hoTa\n" +
        //        "psKdIpaiNkcAeV6myUFPKUCSI+4oyuSDTcfKLhEwLWltxdw/IkaJ5ifEoN0BBOSR\n" +
        //        "bheoYA3tAgMBAAECggEAYZaG/H6h6sYW47T5qEQHMaa/I4rDPWm48qGczQVrv3y1\n" +
        //        "+tARNkDHWINUb7+VgjbwqcS4zWXVhM8AzKbEHdaSJyjPkYiYhamkJGXz6VhtuHmd\n" +
        //        "WXAJaGBI6338MXzIcXPkY/8JPLyISYRmqNTQ3I26C+z7tFdDSdZWomxHieA9EdUW\n" +
        //        "LIcAOs23Gk/kLLox23VOSxaC3v2X/0wk87isTCY7STn3k75Y7TYFHZS2aqiOvrI6\n" +
        //        "1vMgBCEZkN73rRieZARSqW22yaK1U1uAkWzAUK5Cbp2xwbLjFUlNHiuUgQv2WBK9\n" +
        //        "YpElAg9i+gN3DE8iIE8XDTRMYVqBAaUoD9BqgL5+YwKBgQD/JFuw1u+na3VJeqp8\n" +
        //        "OgGxM/2Ml7cctT2+LrRGe1GpCc0Bhp/pnTpvS9NgQk/mlDQTh2ezT4KlD72Lsz3P\n" +
        //        "HubWj9HwzWijRYq13oO98MNJnv9HKAecALSfZVqD/ETU9qdk8PVlsF89+gS94HqE\n" +
        //        "zwbzJb6y1/HBNBkPRH65j313wwKBgQDS9Fw2MTBP8hB4lRnN7NliGSscPr0ocPtU\n" +
        //        "C5TIvV0+fp8qXk31IKUzoLzpOFQxUoaphG82GFNx8vdc5UZWgSgIbuR/xZWsBmcc\n" +
        //        "6y9ahI2PjDqi0u5QWZ8DCvatBy0JZj1BOdVWlQ9tcR+m+LS7OQqSRC0ICY4NluII\n" +
        //        "DBNlgpK4jwKBgG9NvypAOcBBoqLxflo/O+nAEoY40UsWzd6fpUlyFse1XNejkTNq\n" +
        //        "wck3vPirNQwT7zvtwBF7SDUVzsyY3wfyJSXJC/8OvO0tMzvv3G0KEUeWsyqpjdhC\n" +
        //        "foliv8AAV07IY3InpAY+kO4KmfLzcL9rSU7dUnnWtsJgJeaBLP9qV4PdAoGAYr+M\n" +
        //        "YrnH2cSAPVHeUGzsnWUWrxQXS9UHcSIytX0fJt/y9cxLRdHQQ0ic5bc+ghAVhUi/\n" +
        //        "Hl1owlBvxoZ01I2FBd2TDNSa6XndzsWw1vhxo56K3ClM8UPikXQSYJCZXmsDhYLj\n" +
        //        "ALKqFBr+LBUD1RgwgKN1SOvgGTBmAkpU0hG7SLUCgYBNXkV/bXWbirHfEkwm1WjN\n" +
        //        "rjU4R/ZJO1tEqiGY39KGjHg9BdIHs1xYrFouPZS0U/i/ZFSTX02SuhtqXvpu0wCt\n" +
        //        "0fT5gyuEw7CCIKwffgrJFE31m1nvq51DsbNv1AFf2K4cKDT5bkdMgy/8XdwBhkr5\n" +
        //        "EdlAD5u6NAUC2XB6CbjbcQ==\n" +
        //        "-----END PRIVATE KEY-----");
        //props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
        //props.put(SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_CONFIG, "-----BEGIN CERTIFICATE-----\n" +
        //        "MIIFLTCCAxWgAwIBAgIURN85LnY4h4g7/qj7fuwJkQvRvI0wDQYJKoZIhvcNAQEN\n" +
        //        "BQAwLTETMBEGA1UECgwKaW8uc3RyaW16aTEWMBQGA1UEAwwNY2x1c3Rlci1jYSB2\n" +
        //        "MDAeFw0yNDA3MTIyMjMxMjdaFw0yNzA0MDgyMjMxMjdaMC0xEzARBgNVBAoMCmlv\n" +
        //        "LnN0cmltemkxFjAUBgNVBAMMDWNsdXN0ZXItY2EgdjAwggIiMA0GCSqGSIb3DQEB\n" +
        //        "AQUAA4ICDwAwggIKAoICAQC7Pcnryhzzvsj6d0cPHvlM3phjK+zbsIRyHarITm5w\n" +
        //        "PEfW/gjp8Z7IiqQYZUppo1mmnGrWLjai3T7Afz4H7a5SAaJ6+0vjrQHPgNgAdgpb\n" +
        //        "dvzIRR0x8XtE7+OfXggzcJ0NOCwHuyhMd4UaVTiYt7ypkN41TZUKIoFhobL89J8a\n" +
        //        "y4KEyZsenNJ1MMHrKIkjDejNHeTXAfCfiAxRIbiJexqghcMwtiSGzC/gIQdCfR0i\n" +
        //        "3+l0IaQ6wBx7yEymgBq4wM3MDldF/BdCp7F7yAuHeGXYHxncpM5gJcPy9RAzrE55\n" +
        //        "8fMet0nB2D8Dm+orlBzB9eIC1I+5akHwXzb4rZg+h7hvMn1+tNcXGDoYl9nu+IQT\n" +
        //        "KT4F5ap2n/aZdO7X/f/OuhzUQofqLh3QjH/Uzg80//of0VY9CUux0JcKhsuZLlDL\n" +
        //        "h31xW8z7cw3hT4lZ1CjbeRjw/6z05HxUMvVpu2SpQYNpn6F6RiL1UuHuNkOLEurQ\n" +
        //        "2WNsjx8QaNLWAesBaHR3bw4iX2thraBqM2QoIxInKbrlepXUIswXWe9aezz3ni3w\n" +
        //        "AMCoToCOvamWm43hcBibG3di4lZ5GqzDWDcN7qqeqdXyE3ELHZQQFEIlFuitxKSM\n" +
        //        "IBfu/muxj359dTsglb3+d5h0kWEV16m9CLRi3ZSdQU5LvvZaMtCGkM7GQOluaXXO\n" +
        //        "3QIDAQABo0UwQzAdBgNVHQ4EFgQU0bmpma3JXSnfMeWnR/c4b4R3Z/swEgYDVR0T\n" +
        //        "AQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMCAQYwDQYJKoZIhvcNAQENBQADggIB\n" +
        //        "AGNvfXPXTMcd9x59JQVmLmK2tsrI6w9YL+yVK/EbuKn1BOanzQvqigLUD4N47kcc\n" +
        //        "yPDj9oc1RjMSUoXsyWqb0oL1Ccmk5P8gM2g/70WKJpfSmo01rrfDP6h0pOLfxv4J\n" +
        //        "6vjIqfF5+gXOcF8LGD+hjRCemFv+ITo61C0BUViVjgaU64PjqKjOszEzPAjjod/y\n" +
        //        "Du39HvQs1CBWCZvLIXfPsQaBdi0/yRrwEI7/hnwlFNe30VctzTo6pgHvmZXo5NE0\n" +
        //        "dbEuBLL+0nUU1YdV6W1Y29UIm6O/s+A58zUwkCJARHOXe//FOhDAhiViNvumrvao\n" +
        //        "Tsn8d/vQUXHOJ2090cLVMf4qLZVRP3j9sb/sHLOaEmMIrVpj1f5JAfrogEZpmtNm\n" +
        //        "A5hY3lfthhN91p2RTh9tINdMKqJIs3cJ8YqRCh0y0/QRw9b5O4ObtSub7tGT4re0\n" +
        //        "zjNu5nJmY7yGYP3xTthMQe2Looay6YloJ8egLkZdyuUthp0mmNdTaEdCR1NubA0f\n" +
        //        "BHCVoihLI5pR8Fe4p2wuOsMEjY7wbcqSkl2UCO5KOpcw/9ms2hYXpPs1Ucul1Ogd\n" +
        //        "9poDDgwsu4Iuougn2DPN8kUYCcSS8WKochvMpZ4Q8sqxj+0S8i+LuWegHKfYYa5Q\n" +
        //        "cW8Qqybsvt7nh6P2ukjlyZPX0ps1of/csGqNRFm41D+f\n" +
        //        "-----END CERTIFICATE-----");
        //props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, ""); // Hostname verification
        //props.put(AdminClientConfig.RETRIES_CONFIG, "0");

        return AdminClient.create(props);
    }
}
