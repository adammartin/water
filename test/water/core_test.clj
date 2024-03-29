(ns water.core-test
  (:use water.core)
  (:use clojure.test)
  (:use midje.sweet)
)

(def simple [[1 2]
			 [3 5]])

(def blarg (water.core.Position. 1 1))

(def anode (water.core.Node. (water.core.Point. 5 (water.core.Position. 1 1)) (water.core.Point. 2 (water.core.Position. 1 0)) ))

(def firstNode (water.core.Node. (water.core.Point. 1 (water.core.Position. 0 0)) (water.core.Point. 1 (water.core.Position. 0 0)) ))

(fact ( list (:x blarg) (:y blarg) ) => '(1 1) )

(fact (make-position 0 0) => (water.core.Position. 0 0) )

(fact (make-neighbor 0 0 :north 0 0) => nil)

(fact (make-neighbor 1 1 :north 2 2) => (water.core.Position. 1 0))

(fact (make-neighbor 0 0 :south 0 0) => nil)

(fact (make-neighbor 0 0 :south 0 1) => (water.core.Position. 0 1))

(fact (make-neighbor 0 0 :south 0 2) => (water.core.Position. 0 1))

(fact (make-neighbor 0 0 :east 0 0) => nil)

(fact (make-neighbor 0 0 :east 1 0) => (water.core.Position. 1 0))

(fact (make-neighbor 0 0 :east 2 0) => (water.core.Position. 1 0))

(fact (make-neighbor 0 0 :west 0 0) => nil)

(fact (make-neighbor 1 0 :west 1 0) => (water.core.Position. 0 0))

(fact (make-neighbor 2 0 :west 2 0) => (water.core.Position. 1 0))

(fact (make-point 0 0 :north 0 0 [[0]]) => nil)

(fact (make-point 1 1 :north 1 1 [[0 1] [2 3]]) => (water.core.Point. 1 (water.core.Position. 1 0) ))

(fact (make-relative 0 0 :north 0 0 [[0]]) => nil)

(fact (make-relative 1 1 :north 1 1 [[0 1] [2 3]]) => (water.core.RelativePoint. 1
														(water.core.Point. 1 (water.core.Position. 1 0)) :north))

(fact (drains-to? 1 1 [[0 1] [2 3]] 1 1) => (water.core.Point. 1 (water.core.Position. 1 0)) )

(fact (drains-to? 0 1 [[0 1] [2 3]] 1 1) => (water.core.Point. 0 (water.core.Position. 0 0)) )

(fact (drains-to? 0 0 [[0 1] [2 3]] 1 1) => (water.core.Point. 0 (water.core.Position. 0 0)) )

(fact (drains-to? 0 1 [[0 1] [2 3]] 1 1) => (water.core.Point. 0 (water.core.Position. 0 0)) )

(fact (drains-to? 0 1 [[2 3] [1 0]] 1 1) => (water.core.Point. 0 (water.core.Position. 1 1)) )

(fact (drains-to? 1 1 [[0 0 0] [0 0 0] [0 0 0]] 2 2) => (water.core.Point. 0 (water.core.Position. 1 1)) )

(fact (drains-to? 1 1 [[0 0 0] [0 1 0] [0 0 0]] 2 2) => (water.core.Point. 0 (water.core.Position. 1 0)) )

(fact (drains-to? 1 1 [[0 1 0] [0 1 0] [0 0 0]] 2 2) => (water.core.Point. 0 (water.core.Position. 0 1)) )

(fact (drains-to? 1 1 [[0 1 0] [1 1 0] [0 0 0]] 2 2) => (water.core.Point. 0 (water.core.Position. 2 1)) )

(fact (drains-to? 1 1 [[0 1 0] [1 1 1] [0 0 0]] 2 2) => (water.core.Point. 0 (water.core.Position. 1 2)) )

(fact (make-node 1 0 0 1 1 simple) => firstNode)

(fact (make-node 5 1 1 1 1 simple) => anode)

(fact (make-nodes [[1]] 0 0) => (vector (vector (water.core.Node. (water.core.Point. 1 (water.core.Position. 0 0)) (water.core.Point. 1 (water.core.Position. 0 0)) ))))

(fact (make-nodes [[0 1]] 1 0) => [[(make-node 0 0 0 1 0 [[0 1]]) (make-node 1 1 0 1 0 [[0 1]])]])

(def aVec [[0 1] [2 3]])

(fact (make-nodes aVec 1 1) => [[(make-node 0 0 0 1 1 aVec) (make-node 1 1 0 1 1 aVec)]
								[(make-node 2 0 1 1 1 aVec) (make-node 3 1 1 1 1 aVec)]])

(future-fact (populate-drain [[(make-node 0 0 0 0 0)]]) => (conj (vec nil) (conj (vec nil)
					   															 (water.core.Node. 0 (water.core.Position. 0 0)
						   															 				 nil 
					   															 					 nil 
					   															 					 nil
					   															 					 nil
					   															 					 (water.core.Position. 0 0))
						   														)))

(future-fact (populate-drain [[(make-node 0 0 0 1 1) (make-node 1 1 0 1 1)]
					   [(make-node 2 0 1 1 1) (make-node 3 1 1 1 1)]]) => (conj (vec nil) (conj (vec nil)
					   															 (water.core.Node. 0 (water.core.Position. 0 0)
						   															 				 nil 
					   															 					 (water.core.Position. 0 1) 
					   															 					 (water.core.Position. 1 0) 
					   															 					 nil
					   															 					 (water.core.Position. 0 0))
					   															 (water.core.Node. 0 (water.core.Position. 1 0)
						   															 				 nil 
					   															 					 (water.core.Position. 1 1)
					   															 					 nil 
					   															 					 (water.core.Position. 0 0)
					   															 					 (water.core.Position. 0 0))
						   														)
						   													   (conj (vec nil) 
					   															 (water.core.Node. 0 (water.core.Position. 0 1)
					   															 					 (water.core.Position. 0 0) 
						   															 				 nil 
					   															 					 (water.core.Position. 1 1) 
					   															 					 nil
					   															 					 (water.core.Position. 0 0))
					   															 (water.core.Node. 0 (water.core.Position. 1 1)
					   															 					 (water.core.Position. 1 0)
						   															 				 nil
					   															 					 nil 
					   															 					 (water.core.Position. 0 1)
					   															 					 (water.core.Position. 0 0))
							   													)))

(fact (convert-map [[1]]) => [[ "a" ]])

(fact (convert-map [[1] [2]]) => [[ "a" ] [ "a" ]])

; (fact (convert-map [[1] [1]]) => [[ "a" ] [ "b" ]])
