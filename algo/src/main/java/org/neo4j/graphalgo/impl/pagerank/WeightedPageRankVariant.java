/**
 * Copyright (c) 2017 "Neo4j, Inc." <http://neo4j.com>
 *
 * This file is part of Neo4j Graph Algorithms <http://github.com/neo4j-contrib/neo4j-graph-algorithms>.
 *
 * Neo4j Graph Algorithms is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.graphalgo.impl.pagerank;

import org.neo4j.graphalgo.api.*;
import org.neo4j.graphalgo.core.utils.paged.AllocationTracker;

public class WeightedPageRankVariant implements PageRankVariant {
    private boolean cacheWeights;

    public WeightedPageRankVariant(boolean cacheWeights) {
        this.cacheWeights = cacheWeights;
    }


    public ComputeStep createComputeStep(double dampingFactor, int[] sourceNodeIds,
                                         RelationshipIterator relationshipIterator,
                                         WeightedRelationshipIterator weightedRelationshipIterator,
                                         Degrees degrees,
                                         int partitionCount, int start,
                                         DegreeCache degreeCache) {
        if(cacheWeights ){
            return new WeightedWithCachedWeightsComputeStep(
                    dampingFactor,
                    sourceNodeIds,
                    relationshipIterator,
                    degrees,
                    partitionCount,
                    start,
                    degreeCache
            );
        } else {
            return new WeightedComputeStep(
                    dampingFactor,
                    sourceNodeIds,
                    weightedRelationshipIterator,
                    degrees,
                    partitionCount,
                    start,
                    degreeCache
            );
        }
    }

    @Override
    public HugeComputeStep createHugeComputeStep(double dampingFactor, long[] sourceNodeIds,
                                                 HugeRelationshipIterator relationshipIterator, HugeDegrees degrees,
                                                 HugeRelationshipWeights relationshipWeights, AllocationTracker tracker,
                                                 int partitionCount, long start, DegreeCache aggregatedDegrees) {
        return new HugeWeightedComputeStep(
                dampingFactor,
                sourceNodeIds,
                relationshipIterator,
                degrees,
                relationshipWeights,
                tracker,
                partitionCount,
                start,
                aggregatedDegrees
        );
    }

    @Override
    public DegreeComputer degreeComputer(Graph graph) {
            return new WeightedDegreeComputer(graph, cacheWeights);
    }
}
