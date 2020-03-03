ifeq ($(NATIVE),1)
    include Makefile.native
else
    include Makefile.universal
endif

.PHONY: ci-tests
ci-tests: yuck.test.ContinuousIntegrationTestSuite

.PHONY: unit-tests
unit-tests: yuck.test.UnitTestSuite

.PHONY: front-end-tests
front-end-tests: yuck.flatzinc.test.FrontEndTestSuite

.PHONY: minizinc-examples
minizinc-examples: yuck.flatzinc.test.TractableMiniZincExamples

.PHONY: minizinc-challenges
minizinc-challenges: yuck.flatzinc.test.MiniZincChallenges archive.minizinc-challenges

.PHONY: minizinc-benchmarks
minizinc-benchmarks: yuck.flatzinc.test.MiniZincBenchmarks archive.minizinc-benchmarks

COMMIT_DATE = $(shell git log -1 --pretty=format:%cd --date=format:%Y-%m-%d)
COMMIT_HASH = $(shell git rev-parse --short=8 HEAD)
BRANCH = $(shell git rev-parse --abbrev-ref HEAD)
NOW = $(shell date +%Y-%m-%d_%H-%M-%S)

.PHONY: archive.%
archive.%: TAG := run-$(NOW)-$(subst /,-,$(BRANCH))-$(COMMIT_HASH)-$*
archive.%:
	cd logs && mkdir $(TAG) && mv ../tmp/* $(TAG) && tar cjf $(TAG).tar.bz2 $(TAG) && rm -fr $(TAG)
	git tag -f -m $(TAG) $(TAG)
	git push -f origin $(TAG)

.PHONY: stage
stage:
	sbt stage

.PHONY: doc
doc:
	sbt doc

.PHONY: clean
clean:
	rm -fr target

.PHONY: render-readme
render-readme:
	python3 -m grip

# prints the given variable
.PHONY: print.%
print.%:
	@echo $* = $($*)
