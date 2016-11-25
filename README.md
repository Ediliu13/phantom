phantom
[![Build Status](https://travis-ci.org/outworkers/phantom.svg?branch=develop)](https://travis-ci.org/outworkers/phantom) [![Coverage Status](https://coveralls.io/repos/outworkers/phantom/badge.svg)](https://coveralls.io/r/outworkers/phantom)  [![Codacy Rating](https://api.codacy.com/project/badge/grade/25bee222a7d142ff8151e6ceb39151b4)](https://www.codacy.com/app/flavian/phantom_2) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.outworkers/phantom-dsl_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.outworkers/phantom-dsl_2.11) [![Bintray](https://api.bintray.com/packages/outworkers/oss-releases/phantom-dsl/images/download.svg) ](https://bintray.com/outworkers/oss-releases/phantom-dsl/_latestVersion) [![ScalaDoc](http://javadoc-badge.appspot.com/com.outworkers/phantom-dsl_2.11.svg?label=scaladoc)](http://javadoc-badge.appspot.com/com.outworkers/phantom-dsl_2.11) [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/outworkers/phantom?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
===============================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================

Reactive type-safe Scala driver for Apache Cassandra/Datastax Enterprise

To stay up-to-date with our latest releases and news, follow us on Twitter: [@outworkers](https://twitter.com/outworkers_uk).

If you use phantom, please consider adding your company to our list of adopters.
phantom is and will always be [freeware](https://en.wikipedia.org/wiki/Freeware), but the more adopters our projects have, the more people from our company will actively work to make them better.

![phantom](https://s3-eu-west-1.amazonaws.com/websudos/oss/logos/phantom.png "Outworkers Phantom")


2.0.0 Migration guide
=====================

- The OSS version of phantom has as of 2.0.0 returned to the Apache V2 license and the license is here to stay.
- All packages and dependencies are now available under the `com.outworkers` organisation instead of `com.websudos`. As
part of long term re-branding efforts, we have finally felt it's time to make sure the change is consistent throughout.
- There is a new and now completely optional Bintray resolver, `Resolver.bintrayRepo("outworkers", "oss-releases")`,
 that gives you free access to the latest cuts of our open source releases before they hit Maven Central. We assume
 no liability for your usage of latest cuts, but we welcome feedback and we do our best to have elaborate CI processes in place.
- `EnumColumn` is now relying entirely on `Primitive.macroImpl`, which means you will not need to pass in the enumeration
as an argument to `EnumColumn` anymore. This means `object enum extends EnumColumn(this, enum: MyEnum)` is now simply
`object enum extends EnumColumn[MyEnum#Value]`
- All dependencies are now being published to Maven Central. This includes outworkers util and outworkers diesel,
projects which have in their own right been completely open sourced under Apache V2 and made public on GitHub.
- All dependencies on `scala-reflect` have been completely removed.
- A new, macro based mechanism now performs the same auto-discovery task that reflection used to, thanks to `macro-compat`.
- Index modifiers no longer require a type parameter, `PartitionKey`, `PrimaryKey`, `ClusteringOrder` and `Index` don't require
the column type passed anymore.
- `KeySpaceDef` has been renamed to the more appropiate `CassandraConnector`.
- `CassandraConnector` now natively supports specifying a keyspace creation query.
- `TimeWindowCompactionStrategy` is now natively supported in the CREATE/ALTER dsl.
- Collections can now be used as part of a primary or partition key.
- Tuples are now natively supported as valid types via `TupleColumn`.


Using phantom
=============

### Scala 2.10 and 2.11 releases ###

We publish phantom in 2 formats, stable releases and bleeding edge.

- The stable release is always available on Maven Central and will be indicated by the badge at the top of this readme. The Maven Central badge is pointing at the latest version

- Intermediary releases are available through our managed Bintray repository available at `https://dl.bintray.com/outworkers/oss-releases/`. The latest version available on our Bintray repository is indicated by the Bintray badge at the top of this readme.


### How phantom compares

To compare phantom to similar tools in the Scala/Cassandra category, you can read more [here](https://github.com/outworkers/phantom/blob/develop/comparison.md).


### Latest versions

The latest versions are available here. The badges automatically update when a new version is released.

- Latest stable version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.outworkers/phantom-dsl_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.outworkers/phantom-dsl_2.11) (Maven Central)
- Bleeding edge: [![Bintray](https://api.bintray.com/packages/outworkers/oss-releases/phantom-dsl/images/download.svg)](https://bintray.com/outworkers/oss-releases/phantom-dsl/_latestVersion) (OSS releases on Bintray)

<a id="learning-phantom">Tutorials on phantom and Cassandra</a>
======================================================================

For ease of use and far better management of documentation, we have decided to export the `README.md` to a proper
Wiki page, now available [here](https://github.com/outworkers/phantom/wiki/).

The following are the current resources available for learning phantom, outside of tests which are very useful in
highlighting all the possible features in phantom and how to use them.

This is a list of resources to help you learn phantom and Cassandra:

- [ ] [Datastax Introduction to Cassandra](http://www.datastax.com/documentation/getting_started/doc/getting_started/gettingStartedIntro_r.html).
- [ ] [The official Scala API docs for phantom](http://phantom-docs.s3-website-eu-west-1.amazonaws.com/)
- [ ] [The main Wiki](https://github.com/outworkers/phantom/wiki)
- [ ] The StackOverflow [phantom-dsl](http://stackoverflow.com/questions/tagged/phantom-dsl) tag, which we always monitor!
- [ ] Anything tagged phantom on our blog is a phantom tutorial: [phantom tutorials](http://outworkers.com/blog/tag/phantom)
- [ ] [A series on Cassandra: Getting rid of the SQL mentality](http://outworkers.com/blog/post/a-series-on-cassandra-part-1-getting-rid-of-the-sql-mentality)
- [ ] [A series on Cassandra: Indexes and keys](http://outworkers.com/blog/post/a-series-on-cassandra-part-2-indexes-and-keys)
- [ ] [A series on Cassandra: Advanced features](http://outworkers.com/blog/post/a-series-on-cassandra-part-3-advanced-features)
- [ ] [A series on phantom: Getting started with phantom](http://outworkers.com/blog/post/a-series-on-phantom-part-1-getting-started-with-phantom)
- [ ] [The Play! Phantom Activator template](https://github.com/outworkers/phantom-activator-template)
- [ ] [Thiago's Cassandra + Phantom demo repository](https://github.com/thiagoandrade6/cassandra-phantom)


<a id="issues-and-questions">Issues and questions</a>
=====================================================
<a href="#table-of-contents">back to top</a>

We love Cassandra to bits and use it in every bit of our stack. phantom makes it super trivial for Scala users to embrace Cassandra.

Cassandra is highly scalable and it is by far the most powerful database technology available, open source or otherwise.

Phantom is built on top of the [Datastax Java Driver](https://github.com/datastax/java-driver), which does most of the heavy lifting.

We are very happy to help implement missing features in phantom, answer questions about phantom, and occasionally help you out with Cassandra questions! Please use GitHub for any issues or bug reports.

Adopters
========

The following are just some of the biggest phantom adopters, though the full list
is far more comprehensive.

- [Starbucks Corporation](https://www.starbucks.com/)
- [CreditSuisse](https://www.credit-suisse.com/global/en/)
- [ING](http://www.ing.com/en.htm)
- [UBS](https://www.ubs.com/global/en.html)
- [Wincor Nixdorf](http://www.wincor-nixdorf.com/internet/site_EN/EN/Home/homepage_node.html)
- [Paddy Power](http://www.paddypower.com/)
- [Strava](https://www.strava.com/)
- [Mobli](https://www.mobli.com/)
- [Pellucid Analytics](http://www.pellucid.com/)
- [Equens](http://www.equens.com/)
- [outworkers](https://www.outworkers.com/)
- [VictorOps](http://www.victorops.com/)
- [Socrata](http://www.socrata.com)
- [Sphonic](http://www.sphonic.com/)
- [Anomaly42](http://www.anomaly42.com/)
- [Tecsisa](http://www.tecsisa.com/en/)
- [Tuplejump](http://www.tuplejump.com/)
- [FiloDB](http://www.github.com/tuplejump/FiloDB) - the fast analytics database built on Cassandra and Spark
- [Chartboost](https://www.chartboost.com)


License and copyright
======================

Phantom is distributed under the Apache V2 License.

- `Outworkers, Limited` is the copyright holder.

- You can use phantom in commercial products or otherwise.

- We strongly appreciate and encourage contributions.

- All paid for features are published and sold separately as `phantom-pro`, everything that is currently available for free will remain so forever.

If you would like our help with any new content or initiatives, we'd love to hear about it!

<a id="contributors">Contributors</a>
=====================================
<a href="#table-of-contents">back to top</a>

Phantom was developed at outworkers as an in-house project. All Cassandra integration at outworkers goes through phantom, and nowadays it's safe to say most
Scala/Cassandra users in the world rely on phantom.

* Flavian Alexandru ([@alexflav23](https://github.com/alexflav23)) - maintainer
* Bartosz Jankiewicz ([@bjankie1](https://github.com/bjankie1))
* Benjamin Edwards ([@benjumanji](https://github.com/benjumanji))
* Kevin Wright ([@kevinwright](https://github.com/kevinwright))
* Eugene Zhulenev ([@ezhulenev](https://github.com/ezhulenev))
* Michal Matloka ([@mmatloka](https://github.com/mmatloka))
* Thiago Pereira ([@thiagoandrade6](https://github.com/thiagoandrade6))
* Juan José Vázquez ([@juanjovazquez](https://github.com/juanjovazquez))
* Viktor Taranenko ([@viktortnk](https://github.com/viktortnk))
* Stephen Samuel ([@sksamuel](https://github.com/sksamuel))
* Evan Chan ([@evanfchan](https://github.com/velvia))
* Jens Halm ([@jenshalm](https://github.com/jenshalm))
* Donovan Levinson ([@levinson](https://github.com/levinson))

<a id="copyright">Copyright</a>
===============================
<a href="#table-of-contents">back to top</a>

Special thanks to Viktor Taranenko from WhiskLabs, who gave us the original idea.

Copyright &copy; 2013 - 2016 outworkers.

Contributing to phantom
=======================
<a href="#table-of-contents">back to top</a>

Contributions are most welcome! Use GitHub for issues and pull requests and we will happily help out in any way we can!
