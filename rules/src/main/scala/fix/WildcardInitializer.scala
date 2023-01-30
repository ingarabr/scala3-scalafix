/*
 * Copyright 2022 Arktekk
 *
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

package fix

import scalafix.v1._
import scala.meta._

class WildcardInitializer extends SemanticRule("WildcardInitializer") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect { case defn @ Defn.Var(_, _, _, None) =>
      defn.tokens.splitOn(_.is[Token.Equals]) match {
        case Some((_, rest)) =>
          Patch.removeTokens(rest.tail) + Patch.addRight(rest.head, " scala.compiletime.uninitialized")
        case None => Patch.empty
      }
    }.asPatch
  }
}
