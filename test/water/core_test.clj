(ns water.core-test
  (:use water.core)
  (:use clojure.test)
  (:use midje.sweet)
)

(def simple [[1 2]
			 [3 4]])

(def blarg (water.core.Position. 1 1))

(def anode (water.core.Node. 5 (water.core.Position. 1 1) (water.core.Position. 1 0) (water.core.Position. 1 2) (water.core.Position. 2 1)(water.core.Position. 0 1) nil ))

(def firstNode (water.core.Node. 1 (water.core.Position. 0 0) nil nil nil nil nil ))

(fact ( list (:x blarg) (:y blarg) ) => '(1 1) )

(fact (:value anode) => 5 )

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

(fact (make-node 1 0 0 0 0) => firstNode)

(fact (make-node 5 1 1 2 2) => anode)

(fact (make-nodes [[1]] 0 0) => [[firstNode]])

(fact (make-nodes [[0 1]] 1 0) => [[(make-node 0 0 0 1 0) (make-node 1 1 0 1 0)]])

(fact (make-nodes [[0 1] [2 3]] 1 1) => [[(make-node 0 0 0 1 1) (make-node 1 1 0 1 1)]
										 [(make-node 2 0 1 1 1) (make-node 3 1 1 1 1)]])

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
