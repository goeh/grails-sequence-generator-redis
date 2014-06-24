# Grails Redis Sequence Generator Plugin

This plugin is an extension to the [sequence-generator](https://github.com/goeh/grails-sequence-generator) plugin.
This plugin uses Redis "INCR" command to manage sequences.
See [sequence-generator](https://github.com/goeh/grails-sequence-generator) plugin for general information about sequence generators.

**Example**

    sequenceGeneratorService.initSequence('WebOrder', null, null, 100, 'WEB-%04d')

    assert sequenceGeneratorService.nextNumber('WebOrder') == 'WEB-0100'
    assert sequenceGeneratorService.nextNumber('WebOrder') == 'WEB-0101'
    assert sequenceGeneratorService.nextNumber('WebOrder') == 'WEB-0102'

    assert sequenceGeneratorService.nextNumberLong('WebOrder') == 103
    assert sequenceGeneratorService.nextNumberLong('WebOrder') == 104

## Configuration

**sequence.(name).format** (default %d)

Format to use for sequence numbers. The name is the name of the sequence (simple name of the domain class).
The number is formatted with *String#format(String, Object...)*.

    sequence.Customer.format = "%05d"

**sequence.(name).start** (default 1)

The starting number when a sequence is first initialized. The name is the name of the sequence (simple name of the domain class).

    sequence.Customer.start = 1001

## Known Issues

- No known issues.

## Road Map

- Implement sub-sequences. The current implementation does not handle sub-sequences.
