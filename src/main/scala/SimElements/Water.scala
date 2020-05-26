package SimElements

import Utils.Vector2

import scala.collection.mutable

class Water() {

  // Contains water particle data
  private val waterMap: mutable.HashMap[Vector2[Int], WaterParticle] = mutable.HashMap()

  // Apply water physics
  def update(deltaTime: Double, wind: Wind): Unit = {
    var list = List[WaterParticle]()

    for (v <- waterMap.values) {
      list = List.concat(list, v.update(deltaTime))
    }

    // apply wind
    list = applyWind(list, wind)

    //apply collision
    list = applyCollision(list)

    waterMap.clear()
    for (particle <- list){
      waterMap.get(particle.position) match {
        case Some(particleIn) => waterMap.addOne(particle.position, particle + particleIn)
        case None => waterMap.addOne(particle.position, particle)
      }
    }
  }

  def applyWind(list: List[WaterParticle], wind: Wind): List[WaterParticle] = {
    list.map(wind(_)) //  Should work?
  }

  //TODO Apply collision (after other objects and movement)
  def applyCollision(list: List[WaterParticle]): List[WaterParticle] = {
    list
  }

  def initiateWave(position: Vector2[Int], strength: Double): Unit = {
    val x = position.x
    val y = position.y

    for (i <- -1 to 1; j <- -1 to 1) {
      val newPosition = Vector2(x + i, y + j)
      val newForce = Vector2(i, j)
      if (newForce.length() != 0) {
        val particle = WaterParticle(newPosition, Vector2(-1 * strength, 0 * strength), 10) //TODO placeholder values

        waterMap.get(newPosition) match {
          case Some(particleIn) => waterMap.addOne(newPosition, particle + particleIn)
          case None => waterMap.addOne(newPosition, particle)
        }
      }
    }
  }

  // Returns iterable of WaterParticles
  def toIterable : Iterable[WaterParticle] = waterMap.values
}
