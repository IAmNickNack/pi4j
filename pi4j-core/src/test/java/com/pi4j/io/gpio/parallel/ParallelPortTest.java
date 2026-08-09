package com.pi4j.io.gpio.parallel;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.IOType;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputProvider;
import com.pi4j.io.gpio.digital.DigitalOutputProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is not currently a test. It is an example of how ParallelPort might be used.
 */
@Disabled("This is not yet a test")
class ParallelPortTest {

    @Test
    void example() {
        /*
         For now, pretend we have an implementation somehow
         */
        ParallelPortProvider exampleProvider = null;

        /*
         Somehow create a provider in the usual Pi4j manner
         */
        Context context = Pi4J.newContextBuilder()
            .add(exampleProvider)
            .build();


        /*
         Port using non-sequential pins 1 and 10.
         This supports numeric values 0, 1, 2, 3 (0b00, 0b01, 0b10, 0b11)
        */
        ParallelPort port = context.create(ParallelPortConfigBuilder.newInstance()
            .bcm(1)
            .bcm(10)
            .build()
        );

        /*
         This is a provider derived from the parallel port instance.

         It doesn't expose physical pin numbers. The logical pins it accepts are defined by the
         numeric values the underlying port can represent.

         In this case, the port supports 2 logical pins. The offsets for these are 0 and 1
         */
        DigitalInputProvider inputProvider = port.digitalInputProvider();

        DigitalInput input0 = inputProvider.create(0);
        DigitalInput input1 = inputProvider.create(1);

        /*
         This can't be supported if the port has already been used to create an input provider.
        */
        DigitalOutputProvider outputProvider = port.digitalOutputProvider();

    }

}