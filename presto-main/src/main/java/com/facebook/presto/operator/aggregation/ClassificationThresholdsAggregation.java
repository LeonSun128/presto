/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.operator.aggregation;

import com.facebook.presto.operator.aggregation.state.PrecisionRecallState;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.function.AggregationFunction;
import com.facebook.presto.spi.function.AggregationState;
import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.OutputFunction;
import com.facebook.presto.spi.type.DoubleType;

import java.util.Iterator;

@AggregationFunction("classification_thresholds")
@Description("Computes thresholds for precision-recall curves")
public final class ClassificationThresholdsAggregation
        extends PrecisionRecallAggregation
{
    private ClassificationThresholdsAggregation() {}

    @OutputFunction("array(double)")
    public static void output(@AggregationState PrecisionRecallState state, BlockBuilder out)
    {
        Iterator<BucketResult> resultsIterator = getResultsIterator(state);

        BlockBuilder entryBuilder = out.beginBlockEntry();
        while (resultsIterator.hasNext()) {
            final BucketResult result = resultsIterator.next();
            DoubleType.DOUBLE.writeDouble(
                    entryBuilder,
                    (result.left + result.right) / 2);
        }
        out.closeEntry();
    }
}